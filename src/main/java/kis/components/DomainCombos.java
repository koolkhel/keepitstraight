package kis.components;

import kis.data.TreeDAO;
import kis.services.JSONSerializer;
import kis.services.InformationProvider;
import kis.model.Domain;
import org.apache.tapestry5.*;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.util.TextStreamResponse;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Класс будет разворачивать дерево предметных областей в линию
 * при помощи комбобоксов.
 * <p/>
 * При выборе значения в одном комбобоксе появляется следующий,
 * содержащий детей выбранного
 * <p/>
 * Остановиться можно или когда детей больше не будет, или когда
 * захочется
 *
 * @author yury
 */
@IncludeJavaScriptLibrary("cascadecombo1.js")
@IncludeStylesheet("cascadecombo.css")
public class DomainCombos {
    @Inject
    private Logger logger;
    /**
     * нам-то вроде как всё равно, какого типа treeDAO, но
     * что-то система генериков как-то лесом собирается,
     * надо придумать нормальный ОО-способ со всем этим справляться
     */
    @SuppressWarnings("unchecked")
    @Parameter(required = true, name = "source")
    @Property
    private TreeDAO treeDAO;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, value="true")
    @Property
    private boolean multipleRows;

    @Environmental
    private RenderSupport renderSupport;

    @Inject
    private JSONSerializer serializer;

    @Inject
    private InformationProvider info;

    @Inject
    private ComponentResources componentResources;

    private String divId;

    /**
     * ключ -- id вотчера, который мы удаляем или запоминаем
     * значение -- id домена, который в данный момент отмечен в том
     * вотчере
     */
    private Map<Long, Long> watchersMap;

    public DomainCombos() {
        if (watchersMap == null) {
            watchersMap = new HashMap<Long, Long>();
        }
        watchersMap.put(0L, 0L); // сначала есть хотя бы один
    }

    /**
     * в ответ на запрос от сопутствующего JS возвращаем
     * сериализованный список доменов-детей данного элемента
     *
     * @param watcherId id вотчера
     * @param domId ID родителя. -1 == будут запрашиваться корни
     * @return строка, содержащая массив доменов в json-нотации
     */
    @SuppressWarnings("unchecked")
    public StreamResponse onGetChildren(Long watcherId, Long domId) {
        // FIXME вообще, мы много лишней инфы здесь вышлем
        // здесь нужно будет запомнить id текущего запрошенного
        // объекта
        // если не спросили у нас в последний раз -- вероятно,
        // нужен тот объект, который вот в этом списке будет
        // последний
        List<Domain> children;
        logger.warn("пришло: " + watcherId + " домен - " + domId);
        watchersMap.put(watcherId, domId);
        logger.warn(watchersMap.toString());
        if (domId == -1) {
            children = treeDAO.getRootElements();
        } else {
            children = treeDAO.getSubtree(domId);
        }
        List<Domain> cutChildren = cutChildren(children);
        String text = serializer.toJSON(cutChildren);
        StreamResponse sr = new TextStreamResponse("text/javascript", text);
        return sr;
    }

    /**
     * реакция на нажатие ссылки "добавить домен". id новосозданного
     * вотчера отсылается на сервер, чтобы его запомнили
     * @param id
     * @return ""
     */
    public StreamResponse onAddWatcher(Long id) {
        watchersMap.put(id, 0L);
        StreamResponse sr = new TextStreamResponse("text/html", "");
        return sr;
    }

    /**
     * реакция на "убрать домен", чтобы не добавлять показавшееся
     * лишним. шлётся id того, что убрать надо.
     * @param id
     * @return ""
     */
    public StreamResponse onDeleteWatcher(Long id) {
        watchersMap.remove(id);
        StreamResponse sr = new TextStreamResponse("text/html", "");
        return sr;
    }

    @SetupRender
    public void setupRender() {
        divId = renderSupport.allocateClientId("cascadeCombo");
    }

    /**
     * сначала рисуем первый комбобокс
     * @param writer
     */
    @BeginRender
    @SuppressWarnings("unchecked")
    public void beginRender(MarkupWriter writer) {
        List<Domain> domains = cutChildren(treeDAO.getRootElements());
                                            // здесь будет вызов скрипта
        writer.element("div", "id", "cascadeDiv");
        writer.element("div", "id", divId + "0"); // div нужен, чтобы у всех комбобоксов был один
                                // родитель. при этом для второго ряда будет отдельный div
        writer.element("select", "onchange", "cascadeWatchers[0].handleChange(this);",
              "id", "0comboboxIdNo0", "size", Integer.toString(7));

        for (Domain domain : domains) {
            writeDomainOption(writer, domain);
        }
        writer.end(); // select
        if (multipleRows) { // типа так мы избавляемся от многострочности
            writer.element("a", "href", "#", "onclick", "addNewRow()", "id", divId + "0href0");
        } else {
            writer.element("a", "href", "#", "onclick", "addNewRow()", "id", divId + "0href0",
                    "style", "display: none;");
        }
        writer.write("Добавить предметную область");
        writer.end(); // a href


        writer.end(); // div
        writer.end(); // cascadediv
    }

    /**
     * рисуем в markupwriter строчку с option для выбора
     * @param writer
     * @param domain
     */
    protected void writeDomainOption(MarkupWriter writer, Domain domain) {
        Element el = writer.element("option", "value", Long.toString(domain.getId()));
        writer.write(domain.getDescription());
        writer.end();
    }

    @AfterRender
    public void afterRender(MarkupWriter writer) {
        renderSupport.addScript("divId = \"" + divId + "\";");
        renderSupport.addScript("cascadeWatchers.push(new ComboboxWatcher(0, null));");
        // callback для Ajax
        // event link нужен затем, чтобы не париться по поводу того,
        // где в иерархии tapestry находится текущий компонент
        Link el = componentResources.createEventLink("getchildren");
        renderSupport.addScript("cascadeUrl = \"" + info.getServerURL() +
                el.toAbsoluteURI() + "/\";");
        el = componentResources.createEventLink("addwatcher");
        renderSupport.addScript("cascadeAddUrl = \"" + info.getServerURL() +
                el.toAbsoluteURI() + "/\";");
        el = componentResources.createEventLink("deletewatcher");
        renderSupport.addScript("cascadeDeleteUrl = \"" + info.getServerURL() +
                el.toAbsoluteURI() + "/\";");
        // чтобы первый список был раскрыт
        renderSupport.addScript("cascadeWatchers[0].handleChange($('0comboboxIdNo0'));");
    }

    /**
     * уникальный для страницы Id для div'а, в котором будут находиться
     * комбобоксы
     *
     * Поскольку divId потребуется в минимум двух местах -- для отображения
     * и для javascript, генерится он в setupRenders
     * @return
     */
    public String getContainerId() {
        return divId;
    }

    /**
     * поскольку в каждый данный момент нам нужен только 1 срез без
     * зависимостей по детям, то нам не нужны ссылки на детей.
     * Поэтому мы берём срез и убираем в нём ссылки на детей
     * (надеемся, session.flush на это не повлияет)
     * @param args образемый массив
     * @return
     */
    protected List<Domain> cutChildren(List<Domain> args) {
        List<Domain> result = new ArrayList<Domain>();
        for (Domain dom : args) {
            Domain tmp = new Domain();
            tmp.setParent(null);
            tmp.setChildren(null);
            tmp.setDescription(dom.getDescription());
            tmp.setId(dom.getId());
            result.add(tmp);
        }

        return result;
    }

    /**
     * список выбранных доменов. -1 == выбора не сделали
     * @return
     */
    public List<Long> getDomainIds() {
        List<Long> result = new ArrayList<Long>();
        for (Map.Entry<Long, Long> entry : watchersMap.entrySet()) {
            result.add(entry.getValue());
        }
        return result;
    }

    public void reset() {
        watchersMap.clear();
        watchersMap.put(0L, 0L);
    }
}
