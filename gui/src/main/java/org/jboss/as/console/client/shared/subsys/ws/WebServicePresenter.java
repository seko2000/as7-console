package org.jboss.as.console.client.shared.subsys.ws;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import org.jboss.as.console.client.core.NameTokens;
import org.jboss.as.console.client.domain.model.SimpleCallback;
import org.jboss.as.console.client.shared.BeanFactory;
import org.jboss.as.console.client.shared.dispatch.DispatchAsync;
import org.jboss.as.console.client.shared.subsys.RevealStrategy;
import org.jboss.as.console.client.shared.subsys.messaging.model.MessagingProvider;
import org.jboss.as.console.client.shared.subsys.ws.model.WebServiceEndpoint;
import org.jboss.as.console.client.widgets.DefaultWindow;
import org.jboss.as.console.client.widgets.forms.PropertyMetaData;

import java.util.List;

/**
 * @author Heiko Braun
 * @date 6/10/11
 */
public class WebServicePresenter extends Presenter<WebServicePresenter.MyView, WebServicePresenter.MyProxy> {

    private final PlaceManager placeManager;
    private DispatchAsync dispatcher;
    private BeanFactory factory;
    private MessagingProvider providerEntity;
    private DefaultWindow window = null;
    private RevealStrategy revealStrategy;
    private PropertyMetaData propertyMetaData;
    private EndpointRegistry endpointRegistry;

    @ProxyCodeSplit
    @NameToken(NameTokens.WebServicePresenter)
    public interface MyProxy extends Proxy<WebServicePresenter>, Place {
    }

    public interface MyView extends View {
        void setPresenter(WebServicePresenter presenter);
        void updateEndpoints(List<WebServiceEndpoint> endpoints);
    }

    @Inject
    public WebServicePresenter(
            EventBus eventBus, MyView view, MyProxy proxy,
            PlaceManager placeManager,DispatchAsync dispatcher,
            BeanFactory factory, RevealStrategy revealStrategy,
            PropertyMetaData propertyMetaData, EndpointRegistry registry) {
        super(eventBus, view, proxy);

        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        this.factory = factory;
        this.revealStrategy = revealStrategy;
        this.propertyMetaData = propertyMetaData;
        this.endpointRegistry = registry;
    }

    @Override
    protected void onBind() {
        super.onBind();
        getView().setPresenter(this);
    }


    @Override
    protected void onReset() {
        super.onReset();

        loadEndpoints();
    }

    private void loadEndpoints() {
        endpointRegistry.create().refreshEndpoints(new SimpleCallback<List<WebServiceEndpoint>>() {
            @Override
            public void onSuccess(List<WebServiceEndpoint> result) {
                getView().updateEndpoints(result);
            }
        });
    }

    @Override
    protected void revealInParent() {
        revealStrategy.revealInParent(this);
    }
}
