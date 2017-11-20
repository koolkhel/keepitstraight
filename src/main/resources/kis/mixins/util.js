/**
 * Обновление размера textarea по мере добавления в него текста
 * @param sender textarea, для которого назначено событие
 */
function updateTextAreaSize(sender, step) {
        var text = sender.value.replace(/\s+$/g,""); // на концах убираем пробелы
        var rows = text.split('\n');
        var rowsCount = rows.length;
        var currentTextAreaSize = rowsCount + step;

        sender.style.height = currentTextAreaSize + "em";
    }