import org.iesalandalus.programacion.utilidades.Entrada;

public class Menu {

    public static void main(String[] args) {

        Lector.abrirFichero();

        int opcion;
        do {
            mostrarMenu();
            opcion = Entrada.entero();

            switch (opcion) {
                case 1: insertar();           break;
                case 2: consultar();          break;
                case 3: actualizarEmpleados(); break;
                case 4: actualizarNombreLocalidad(); break;
                case 5: eliminar();           break;
                case 6: Lector.listadoDepartamentos(); break;
                case 7: Lector.inspeccionFisica();    break;
                case 8: Lector.estadisticas();        break;
                case 9: Lector.integridad();          break;
                case 0: System.out.println("Chao"); break;
                default: System.out.println("No sirve");
            }
        } while (opcion != 0);

        Lector.cerrarFichero();
    }

    private static void mostrarMenu() {
        System.out.println("MENÚ PRINCIPAL");
        System.out.println("1. Dar de alta departamento");
        System.out.println("2. Buscar departamento");
        System.out.println("3. Cambiar empleados");
        System.out.println("4. Modificar nombre y localidad");
        System.out.println("5. Borrar departamento");
        System.out.println("6. Ver listado");
        System.out.println("7. Inspección física");
        System.out.println("8. Ver estadísticas");
        System.out.println("9. Test de integridad");
        System.out.println("0. Salir");
        System.out.print("Elige una opción: ");
    }

    private static void insertar() {
        System.out.println("Insertar");
        short num = pedirNumero();
        System.out.print("Nombre: ");
        String nombre = Entrada.cadena();
        System.out.print("Localidad: ");
        String localidad = Entrada.cadena();
        short emp = pedirEmpleados();
        Lector.insertaDepartamento(num, nombre, localidad, emp);
    }

    private static void consultar() {
        System.out.println("Consultar");
        Lector.consultaDepartamento(pedirNumero());
    }

    private static void actualizarEmpleados() {
        System.out.println("Actualizar empleados");
        short num = pedirNumero();
        short emp = pedirEmpleados();
        Lector.actualizaNumeroEmpleados(num, emp);
    }

    private static void actualizarNombreLocalidad() {
        System.out.println("Actualizar nombre y localidad");
        short num = pedirNumero();
        System.out.print("Nuevo nombre: ");
        String nombre = Entrada.cadena();
        System.out.print("Nueva localidad: ");
        String localidad = Entrada.cadena();
        Lector.actualizaNombreLocalidad(num, nombre, localidad);
    }

    private static void eliminar() {
        System.out.println("Eliminar departamento");
        short num = pedirNumero();

        System.out.print("¿Confirmar eliminación? (s/n): ");
        String confirmacion = Entrada.cadena();

        if (confirmacion.equalsIgnoreCase("s")) {
            Lector.eliminaDepartamento(num);
        } else {
            System.out.println("Operación abortada.");
        }
    }

    private static short pedirNumero() {
        int n;
        do {
            System.out.print("Numero de departamento (1-32767): ");
            n = Entrada.entero();
        } while (n < 1 || n > 32767);
        return (short) n;
    }

    private static short pedirEmpleados() {
        int n;
        do {
            System.out.print("Numero de empleados: ");
            n = Entrada.entero();
        } while (n < 0);
        return (short) n;
    }
}