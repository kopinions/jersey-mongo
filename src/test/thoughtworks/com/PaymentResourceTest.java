package thoughtworks.com;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import thoughtworks.com.domain.Order;
import thoughtworks.com.domain.User;
import thoughtworks.com.exception.PaymentNotFound;
import thoughtworks.com.repository.UserRepository;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaymentResourceTest extends JerseyTest {

    @Mock
    UserRepository userRepository;

    @Override
    protected Application configure() {
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.packages("thoughtworks.com");
        resourceConfig.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(userRepository).to(UserRepository.class);
            }
        });
        return resourceConfig;
    }

    @Test
    public void should_return_200_when_get_payment() {
        Response response = target("/users/1/orders/2/payment").request().get();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void should_return_404_when_not_found_payment() {
        when(userRepository.getUserOrderPayment(any(User.class), any(Order.class))).thenThrow(PaymentNotFound.class);
        Response response = target("/users/1/orders/2/payment").request().get();

        assertThat(response.getStatus(), is(404));
    }
}
