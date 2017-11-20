package kis.services;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import kis.model.Domain;

import java.util.List;

/**
 * Класс занимается взаимообратными превращениями объектов
 * в json-нотацию. Пока что только для доменов, потом может
 * быть имеет смысл расширить
 *
 * @author yury
 */
public class JSONSerializer {
    private XStream xstream;

    public JSONSerializer() {
        xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.alias("domain", Domain.class);
        xstream.setMode(XStream.NO_REFERENCES);
    }

    public String toJSON(List<Domain> list) {
        return xstream.toXML(list);
    }

    /**
     * FIXME
     * здесь есть бага. если в списке 1 элемент, то он рендерится уже не списком,
     * а просто элементом. Из-за чего на той стороне возникают непонятки.
     *
     * Пока исправлено кодом в js, но вообще надо чинить здесь
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Domain> fromJSON(String json) {
        // FIXME может съесть весь мозг, но надо смотреть
        return (List<Domain>) xstream.fromXML(json);
    }
}
