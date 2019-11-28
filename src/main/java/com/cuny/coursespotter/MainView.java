package com.cuny.coursespotter;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.List;

@Route
@CssImport("style.css")
public class MainView extends VerticalLayout {

    private TextField courseNumber = new TextField();
    private NumberField phoneNumber = new NumberField();
    private EmailField emailAddress = new EmailField();
    private Select<String> college = new Select<>();
    private Select<String> subject = new Select<>();

    private Button submit = new Button("submit");

    private FormLayout formLayout = new FormLayout();

    public MainView() {
        VerticalLayout main = new VerticalLayout();
        main.setSpacing(true);

        Image image = new Image("./logo.png", "CourseSpotter");
        image.setWidth("100px");
        image.setHeight("100px");

        H2 h2 = new H2("CourseSpotter");
        main.add(image);
        main.add(h2);
        initForm();

        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        submit.addClickListener(e -> {
            Notification.show("Form submitted.");
        });

        main.add(formLayout);
        main.add(submit);

        add(main);
    }

    private void initForm() {
        emailAddress.setClearButtonVisible(true);
        emailAddress.setErrorMessage("Please provide a valid Email Address.");
        List<String> Colleges = Arrays.asList("Baruch College", "Borough of Manhattan CC", "Bronx CC", "Brooklyn College", "City College", "College of Staten Island", "Graduate Center", "Guttman CC", "Hostos CC", "Hunter College",
                "John Jay College", "Kingsborough CC", "LaGuardia CC", "Lehman College", "Macaulay Honors College", "Medgar Evers College", "NYC College of Technology", "Queens College", "Queensborough CC",
                "School of Journalism", "School of Labor & Urban Studies", "School of Law", "School of Medicine", "School of Professional Studies", "School of Public Health", "York College");
        college.setItems(Colleges);

        formLayout.addFormItem(college, "College");
        formLayout.addFormItem(subject, "Subject");
        formLayout.addFormItem(courseNumber, "Course Number");
        formLayout.addFormItem(phoneNumber, "Phone");
        formLayout.addFormItem(emailAddress, "Email");

        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("600px", 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
    }
}