package uqvirutal.edu.co.cinemauq.model;

public abstract class Usuario {
    protected String id;
    protected String nombre;
    protected String correo;
    protected String contraseña;


    public Usuario(String id, String nombre, String correo, String contraseña) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contraseña = contraseña;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
    /**
     * Metodo para iniciar Sesión
     */
    public boolean iniciarSesion(String correoIngresado, String contraseñaIngresada) {
        if (this.correo.equalsIgnoreCase(correoIngresado) && this.contraseña.equals(contraseñaIngresada)) {
            System.out.println("Sesión iniciada para: " + this.nombre);
            return true;
        } else {
            System.out.println("Credenciales incorrectas.");
            return false;
        }
    }
    /**
     * Metodo para Cerrar sesión
     */
    public void cerrarSesion() {
        System.out.println("Sesión cerrada para: " + this.nombre);
    }
}