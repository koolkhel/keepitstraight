package kis.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.OptionGroupModelImpl;
import org.apache.tapestry5.util.AbstractSelectModel;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.ioc.services.PropertyAccess;

/*
public class SelectTest {
    @Persist
    private SomeBean _someBean;
    @Inject
    private PropertyAccess _access;

    private GenericSelectModel<SomeBean> _beans;

    public SelectTest(){
        ArrayList<SomeBean> list = new ArrayList<SomeBean>();
        list.add(new SomeBean(1L,"Mirko"));
        list.add(new SomeBean(2L,"Slavko"));
        list.add(new SomeBean(3L,"Jozo"));
        _beans = new GenericSelectModel<SomeBean>(list,SomeBean.class,"name","id",_access);
    }

    public SomeBean getSomeBean(){
       return _someBean;
    }

    public void setSomeBean(SomeBean _someBean){
       this._someBean = _someBean;
    }

    public GenericSelectModel<SomeBean> getBeans(){
       return _beans;
    }
}


<html xmlns:t="http://tapestry.apache.org/schema/tapestry_5_0_0.xsd">
    <head>
            <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
        <title>Select test</title>
    </head>
    <body style="font-family:Courier new">

        <form t:type="Form">
                <t:select model="beans" encoder="beans" value="someBean"/>
                <t:submit/>
        </form>
value: ${someBean}
    </body>
</html>

 */

/** Generic selection model for a list of Objects.
 * use:
 * <pre>@Inject private PropertyAccess _access;</pre>
 * in your page to ge the {@link PropertyAccess} service.<br>
 * !Notice: you must set the created instance both as model and encoder parameter for the Select component.*/
public class GenericSelectModel<T> extends AbstractSelectModel implements ValueEncoder<T> {

    private PropertyAdapter labelFieldAdapter;
    private PropertyAdapter idFieldAdapter;
    private Collection<T>         list;
    private List<OptionGroupModel> optionGroups;

    public GenericSelectModel(Collection<T> list, Class<T> clazz, String labelField, String idField, PropertyAccess access) {
        this.list = list;
        if (idField != null)
            this.idFieldAdapter = access.getAdapter(clazz).getPropertyAdapter(idField);
        if (labelField != null)
            this.labelFieldAdapter = access.getAdapter(clazz).getPropertyAdapter(labelField);
    }

    public void addOptionGroup(String label, boolean disabled, List<T> options) {
        List<OptionModel> optionModels = new ArrayList<OptionModel>();
        if (labelFieldAdapter == null) {
            for (T obj : options) {
                optionModels.add(new OptionModelImpl(nvl(obj), obj));
            }
        } else {
            for (T obj : options) {
                optionModels.add(new OptionModelImpl(nvl(labelFieldAdapter.get(obj)), obj));
            }
        }

        if (optionGroups == null) {
            optionGroups = new ArrayList<OptionGroupModel>();
        }

        optionGroups.add(new OptionGroupModelImpl(label, disabled, optionModels, new String[0]));
    }

    public List<OptionGroupModel> getOptionGroups() {
        return null;
    }

    public List<OptionModel> getOptions() {
        List<OptionModel> optionModelList = new ArrayList<OptionModel>();
        if (labelFieldAdapter == null) {
            for (T obj : list) {
                optionModelList.add(new OptionModelImpl(nvl(obj)));
            }
        } else {
            for (T obj : list) {
                optionModelList.add(new OptionModelImpl(nvl(labelFieldAdapter.get(obj)), obj));
            }
        }
        return optionModelList;
    }

    // ValueEncoder functions
    public String toClient(T obj) {
        if (idFieldAdapter == null) {
            return obj + "";
        } else {
            return idFieldAdapter.get(obj) + "";
        }
    }

    public T toValue(String string) {
        if (idFieldAdapter == null) {
            for (T obj : list) {
                if (nvl(obj).equals(string)) return obj;
            }
        } else {
            for (T obj : list) {
                if (nvl(idFieldAdapter.get(obj)).equals(string)) return obj;
            }
        }
        return null;
    }

    private String nvl(Object o) {
        if (o == null)
            return "";
        else
            return o.toString();
    }
}
