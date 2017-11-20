// и хз, что тут писать :)
// вроде как у нас есть скриптакулус и прототип
/*
 * поэтому действуем очень просто
 * 1. есть реестр связанных объектов, которые обслуживают onSelect
 * в компоненте select
 * 2. у каждого такого элемента есть свой id, чтобы он мог понять,
 * к какому именно select он приписан
 * 3. для первого комбобокса создаётся такой связанный класс, который
 * будет обслуживать
 * 4. для каждого нового onSelect, создаём ещё один combobox средстами
 * javascript, заполняем его данными из AJAX-запроса по id родителя
 * и создаём новый объект, который будет следить за onchange
 * для новосозданного комбобокса.
 */
var cascadeComboboxesCount = 0;
var cascadeWatchers = new Array();
var cascadeUrl = "";
var cascadeAddUrl = "";
var cascadeDeleteUrl = "";
var curObj = null; // чтобы запоминать, какой объект сейчас js отрабатывает
var divId = null; // id дива, в котором все комбобоксы; на всякий случай
var debug = true;
var outerDiv = "cascadeDiv";
/*
 * Итого, требуется:
 *
 * Реестр объектов, следящих за onchange для комбобоксов, чтобы
 * для каждого данного объекта-наблюдателя знать, какие у него в подчинении
 */

function ComboboxWatcher(domainId, parent) {
    // следующий элемент
    this.next = null;
    // в пределах одного ряда, вроде как, постоянен
    if (parent == null) {
        this.id = cascadeComboboxesCount; // для ajax запросов к серверу
    } else {
        this.id = parent.id;
    }
    this.comboboxId = this.id + "comboboxIdNo" + domainId;
    this.divId = divId + this.id;
    this.hrefDeleted = false;
    this.first = (domainId == 0);
}

ComboboxWatcher.prototype = {
    /**
     * удаляем сначала детей, потом себя из dom-картинки tapestry
     */
    deleteChildrenAndSelf: function () {
        // типа рекурсивный спуск, который упирается в последний комбобокс
        if (this.next != null) {
            this.next.deleteChildrenAndSelf();
            this.next = null;
        }
        // удаляем combobox из страницы
        if (!this.first) { // первый удалять не нужно ни при каких обстоятельствах
            $(this.divId).removeChild($(this.comboboxId));
        }
    },
    deleteChildren: function() {
        if (this.next != null) {
            this.next.deleteChildrenAndSelf();
            this.next = null;
        }
    },
    /**
     * уходим с сервера
     */
    unregister: function() {
        new Ajax.Request(cascadeDeleteUrl + this.id, {
            method: 'get',
            evalJSON: false,
            evalJS: false,
            onSuccess: function(request) {

            }
        });
    },
    /**
     * удаляемся и со страницы, и с сервера
     */
    deleteRoot: function() {
        // удаляем дочерние комбобоксы
        this.deleteChildrenAndSelf();
        // удаляем первый комбобокс
        $(this.divId).removeChild($(this.comboboxId));
        // убираем с сервера
        this.unregister();
        // убираем из переменных
        delete cascadeWatchers[this.id]; // splice не подходит,
        // т.к. нам выгоднее, чтобы оно росло
        // судя по всему, на сервере нужен будет Map какой-нибудь, чтобы пропуски удалять
        // не нужно было
    },
    /**
     * обрабатываем сообщение от данного combobox. Если оно не от
     * нашего, то перебазируем его детям
     * @param sender
     */
    handleChange: function (sender) {
        if (this.comboboxId != sender.id) {
            if (this.next != null) {
                this.next.handleChange(sender);
                return;
            }
        }
        // делаем AJAX запрос
        // предполагается, что sender == combobox, поэтому есть selectedIndex
        if (sender.selectedIndex == -1) {
            return;
        }
        var url = cascadeUrl + this.id + "/" + sender.options[sender.selectedIndex].value;

        var list = null; // по идее list повится после выполнения запроса
        curObj = this;
        new Ajax.Request(url, {
            method: 'get',
            evalJSON: false,
            evalJS: false,
            onSuccess: function(request) {
                eval("var data = " + request.responseText);
                // удаляем детей, если есть
                curObj.deleteChildren();

                // пришёл пустой ответ
                if (data.list.size() == 0) {
                    return;
                }

                if (((typeof data.list[0].domain.size != 'undefined') && data.list[0].domain.size() == 0)) {
                    return;
                }

                var bug = false;

                if ((typeof data.list[0].domain.size == 'undefined') &&
                    (typeof data.list[0].domain.description == 'undefined')) {
                    return;
                } else if (typeof data.list[0].domain.description != 'undefined') {
                    bug = true;
                }

                // TODO важное место для всего
                // создаём новый combobox-дитёнок
                var newCombobox = document.createElement("select");
                var domainId = sender.options[sender.selectedIndex].value;
                newCombobox.setAttribute("id", curObj.id + "comboboxIdNo" + domainId);
                // заполняем его данными, которые получили по AJAX-запросу
                // попутно с дитёнком создаём себе .next, чтобы обрабатывать
                // onChange для дитёнка
                curObj.next = new ComboboxWatcher(domainId, curObj);
                newCombobox.setAttribute("onchange", "cascadeWatchers[" + curObj.id + "].handleChange(this);");
                //newCombobox.setAttribute("size", data.list[0].domain.size());
                newCombobox.setAttribute("size", 7);
                newCombobox.setAttribute("class", "cascadeCombobox");
                if (!bug) {
                    for (var counter = 0; counter < data.list[0].domain.size(); counter++)
                    {
                        var option = document.createElement("option");
                        option.appendChild(document.createTextNode(data.list[0].domain[counter].description));
                        option.setAttribute("value", data.list[0].domain[counter].id);
                        newCombobox.appendChild(option);
                    }
                } else {
                    var option = document.createElement("option");
                    option.appendChild(document.createTextNode(data.list[0].domain.description));
                    option.setAttribute("value", data.list[0].domain.id);
                    newCombobox.appendChild(option);
                }
                // вставляем после текущего комбобокса, чтобы надпись на месте оставалась
                if (!curObj.hrefDeleted) {
                    $(curObj.divId).insertBefore(newCombobox, $(curObj.divId + "href" + curObj.id));
                } else {
                    $(curObj.divId).appendChild(newCombobox);
                }

                curObj = null;
            },
            onFailure: function(request) {
                $('databox').innerHTML = "error failure!";
            }
        });
    }
};

function deleteRow(number) {
    var divId = cascadeWatchers[number].divId;
    cascadeWatchers[number].deleteRoot();
    $(outerDiv).removeChild($(divId));
}

/**
 * центральная функция для многострочности этих всех комбобоксов
 */
function addNewRow() {
    cascadeWatchers[cascadeComboboxesCount].hrefDeleted = true;
    cascadeComboboxesCount++;

    var div = document.createElement("div");
    div.setAttribute("id", divId + cascadeComboboxesCount);
    // нужно ещё скрыть ссылку для предыдущего дива
    var prevHref = $(divId + (cascadeComboboxesCount - 1) + "href" + (cascadeComboboxesCount - 1));
    prevHref.setAttribute("onclick", "deleteRow(" + (cascadeComboboxesCount - 1) + ")");
    prevHref.childNodes[0].data = 'Удалить предметную область';

    //$(divId + (cascadeComboboxesCount - 1))
    //        .removeChild($(divId + (cascadeComboboxesCount - 1) + "href" + (cascadeComboboxesCount - 1)));
    // регистрируется при создании
    var watcher = new ComboboxWatcher(0, null);
    // регистрируемся
    new Ajax.Request(cascadeAddUrl + watcher.id, {
        method: 'get',
        evalJSON: false,
        evalJS: false,
        onSuccess: function(request) {
            // дада, всё хорошо, нам пох, ответ-то пустой
        }
    });
    cascadeWatchers.push(watcher);

    var url = cascadeUrl + watcher.id + "/-1"; // -1 == получить корни

    var list = null; // по идее list повится после выполнения запроса

    new Ajax.Request(url, {
        method: 'get',
        evalJSON: false,
        evalJS: false,
        onSuccess: function(request) {
            eval("var data = " + request.responseText);

            // пустое вроде как прийти в этом случае не может
            var newCombobox = document.createElement("select");
            newCombobox.setAttribute("id", watcher.id + "comboboxIdNo0");
            newCombobox.setAttribute("onchange", "cascadeWatchers[" + watcher.id + "].handleChange(this);");
            newCombobox.setAttribute("size", 7);
            newCombobox.setAttribute("class", "cascadeCombobox");
            for (var counter = 0; counter < data.list[0].domain.size(); counter++)
            {
                var option = document.createElement("option");
                option.appendChild(document.createTextNode(data.list[0].domain[counter].description));
                option.setAttribute("value", data.list[0].domain[counter].id);
                newCombobox.appendChild(option);
            }
            div.appendChild(newCombobox);
            var href = document.createElement("a");
            href.setAttribute("id", watcher.divId + "href" + watcher.id);
            href.setAttribute("onclick", "addNewRow()");
            href.setAttribute("href", "#");
            href.appendChild(document.createTextNode("Добавить предметную область"));
            div.appendChild(href);
            $(outerDiv).appendChild(div);
        }
    });
}


// честь создания первого экземпляра лежит на tapestry
// так же тапестря должна сказать postbackURL для AJAX-запроса,
// потому что js не в состоянии сам догадаться, кого пинать (хотя можно как-нибудь
// через browser.window, но лучше так не делать)