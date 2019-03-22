package de.stl.saar.internetentw1.view;

import de.stl.saar.internetentw1.model.User;
import de.stl.saar.internetentw1.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class LoginView implements Serializable {

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

    @Getter
    private User user;

    private final UserRepository userRepository;

    @Inject
    public LoginView(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void clearForm(ComponentSystemEvent event) {
        username = "";
        password = "";
        user = null;
    }

    public String login() {
        User user = userRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            this.user = user;
            return "menu?faces-redirect=true";
        }

        String messageText = "Authentifizierung fehlgeschlagen!";
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", messageText);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, message);

        return "index";
    }
}
