package controller;

class RegisterFormControllerTest {

    public static void main(String[] args) {
        RegisterFormController ctrl = new RegisterFormController();
        assert ctrl.isName("Kasun Nuwan");
        assert (ctrl.isName("Kasun123") == false);
        assert (ctrl.isName("Ka#kdsa") == false);
    }

}