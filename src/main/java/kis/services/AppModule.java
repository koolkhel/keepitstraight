package kis.services;

import kis.data.DomainDAO;
import kis.data.FactDAO;
import kis.data.ProjectDAO;
import kis.data.AttachedFileDAO;
import kis.data.impl.DomainDAOImpl;
import kis.data.impl.FactDAOImpl;
import kis.data.impl.ProjectDAOImpl;
import kis.data.impl.AttachedFileDAOImpl;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.hibernate.HibernateTransactionDecorator;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;
import org.hibernate.Session;

import java.io.IOException;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
public class AppModule {
    public static void bind(ServiceBinder binder) {
        // binder.bind(MyServiceInterface.class, MyServiceImpl.class);

        // Make bind() calls on the  binder object to define most IoC services.
        // Use service builder methods (example below) when the implementation
        // is provided inline, or requires more initialization than simply
        // invoking the constructor.

        // пишет, что если делать одним классом, то не будет декорироваться
        // т.е. гемор с @CommitAfter решается только вот так, не очень рационально

        // данные
        binder.bind(FactDAO.class, FactDAOImpl.class);
        // domaindao собирается в билдере экраном ниже
        // binder.bind(DomainDAO.class, DomainDAOImpl.class);
        binder.bind(ProjectDAO.class, ProjectDAOImpl.class);
        binder.bind(AttachedFileDAO.class, AttachedFileDAOImpl.class);

        // сервисы
        binder.bind(JSONSerializer.class);
        binder.bind(InformationProvider.class);
        binder.bind(TextileWeaver.class);
    }


    public static void contributeApplicationDefaults(
            MappedConfiguration<String, String> configuration) {
        // Contributions to ApplicationDefaults will override any contributions to
        // FactoryDefaults (with the same key). Here we're restricting the supported
        // locales to just "en" (English). As you add localised message catalogs and other assets,
        // you can extend this list of locales (it's a comma separated series of locale names;
        // the first locale name is the default when there's no reasonable match).

        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en");

        // The factory default is true but during the early stages of an application
        // overriding to false is a good idea. In addition, this is often overridden
        // on the command line as -Dtapestry.production-mode=false
        configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
        configuration.add(SymbolConstants.COMPRESS_WHITESPACE, "false");
        // всё равно не помогает
        configuration.add("tapestry.hibernate.early-startup", "true");
    }


    /**
     * This is a service definition, the service will be named "TimingFilter". The interface,
     * RequestFilter, is used within the RequestHandler service pipeline, which is built from the
     * RequestHandler service configuration. Tapestry IoC is responsible for passing in an
     * appropriate Logger instance. Requests for static resources are handled at a higher level, so
     * this filter will only be invoked for Tapestry related requests.
     * <p/>
     * <p/>
     * Service builder methods are useful when the implementation is inline as an inner class
     * (as here) or require some other kind of special initialization. In most cases,
     * use the static bind() method instead.
     * <p/>
     * <p/>
     * If this method was named "build", then the service id would be taken from the
     * service interface and would be "RequestFilter".  Since Tapestry already defines
     * a service named "RequestFilter" we use an explicit service id that we can reference
     * inside the contribution method.
     */
    public RequestFilter buildTimingFilter(final Logger log) {
        return new RequestFilter() {
            public boolean service(Request request, Response response, RequestHandler handler)
                    throws IOException {
                long startTime = System.currentTimeMillis();

                try {
                    // The responsibility of a filter is to invoke the corresponding method
                    // in the handler. When you chain multiple filters together, each filter
                    // received a handler that is a bridge to the next filter.

                    return handler.service(request, response);
                }
                finally {
                    long elapsed = System.currentTimeMillis() - startTime;

                    log.info(String.format("Request time: %d ms", elapsed));
                }
            }
        };
    }

    public DomainDAO buildDomainDao(final Logger logger, final Session session) {
        return new DomainDAOImpl(logger, session);
    }

    /**
     * This is a contribution to the RequestHandler service configuration. This is how we extend
     * Tapestry using the timing filter. A common use for this kind of filter is transaction
     * management or security. The @Local annotation selects the desired service by type, but only
     * from the same module.  Without @Local, there would be an error due to the other service(s)
     * that implement RequestFilter (defined in other modules).
     */
    public void contributeRequestHandler(OrderedConfiguration<RequestFilter> configuration,
                                         @Local
                                         RequestFilter filter) {
        // Each contribution to an ordered configuration has a name, When necessary, you may
        // set constraints to precisely control the invocation order of the contributed filter
        // within the pipeline.

        configuration.add("Timing", filter);
    }

    public static void contributeHibernateEntityPackageManager(
            Configuration<String> configuration) {
        configuration.add("kis.model");
    }

    @Match("*DAO")
    public static <T> T decorateTransactionally(
            HibernateTransactionDecorator decorator, Class<T> serviceInterface,
            T delegate, String serviceId) {
        return decorator.build(serviceInterface, delegate, serviceId);
    }


}
