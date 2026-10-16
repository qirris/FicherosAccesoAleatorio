import java.io.IOException;
import java.io.RandomAccessFile;

public class Lector {

    static final int LONGITUD_REGISTRO = 64;
    static RandomAccessFile fichero;

    public static void abrirFichero() {
        try {
            fichero = new RandomAccessFile("AleatorioDepart.dat", "rw");
            System.out.println("Fichero abierto.");
        } catch (IOException e) {
            System.out.println("Error al abrir el fichero.");
        }
    }

    public static void cerrarFichero() {
        try {
            fichero.close();
        } catch (IOException e) {
            System.out.println("Error al cerrar el fichero.");
        }
    }

    public static boolean existeDepartamento(short num) {
        long pos = (num - 1) * LONGITUD_REGISTRO;
        try {
            fichero.seek(pos);
            short id = fichero.readShort();
            return id == num;
        } catch (IOException e) {
            return false;
        }
    }

    public static void consultaDepartamento(short num) {
        if (!existeDepartamento(num)) {
            System.out.println("El departamento " + num + " no existe.");
            return;
        }
        try {
            fichero.seek((num - 1) * LONGITUD_REGISTRO);
            short id = fichero.readShort();
            String nombre = leerTexto();
            String localidad = leerTexto();
            short empleados = fichero.readShort();

            System.out.println("Numero: " + id);
            System.out.println("Nombre: " + nombre.trim());
            System.out.println("Localidad: " + localidad); // Sin trim() por despiste
            System.out.println("Empleados: " + empleados);
        } catch (IOException e) {
            System.out.println("Error al consultar el departamento.");
        }
    }

    // Inserta un nuevo departamento
    public static void insertaDepartamento(short num, String nombre, String localidad, short empleados) {
        if (existeDepartamento(num)) {
            System.out.println("El departamento " + num + " ya existe.");
            return;
        }

        nombre = nombre.trim();
        localidad = localidad.trim();

        if (nombre.isEmpty() || nombre.length() > 15 || localidad.isEmpty() || localidad.length() > 15) {
            System.out.println("Datos no validos.");
            return;
        }

        try {
            long posicionesActuales = fichero.length() / LONGITUD_REGISTRO;

            for (long i = posicionesActuales; i < num - 1; i++) {
                fichero.seek(i * LONGITUD_REGISTRO);
                fichero.writeShort(0);
                fichero.writeChars("---------------");
                fichero.writeChars("---------------");
                fichero.writeShort(0);
            }

            fichero.seek((num - 1) * LONGITUD_REGISTRO);
            fichero.writeShort(num);
            StringBuffer bufNombre = new StringBuffer(nombre);
            bufNombre.setLength(15);
            fichero.writeChars(bufNombre.toString());
            StringBuffer bufLocalidad = new StringBuffer(localidad);
            bufLocalidad.setLength(15);
            fichero.writeChars(bufLocalidad.toString());
            fichero.writeShort(empleados);
            System.out.println("Departamento " + num + " insertado.");

        } catch (IOException e) {
            System.out.println("Error al insertar el departamento.");
        }
    }

    // Borrado logico
    public static void eliminaDepartamento(short num) {
        try {
            fichero.seek((num - 1) * LONGITUD_REGISTRO);
            fichero.writeShort(-1);
            fichero.writeChars("***************");
            fichero.writeChars("***************");
            fichero.writeShort(-1);
            System.out.println("Departamento " + num + " eliminado.");
        } catch (IOException e) {
            System.out.println("Error al eliminar el departamento.");
        }
    }

    // Actualiza solo el numero de empleados
    public static void actualizaNumeroEmpleados(short num, short empleados) {
        if (!existeDepartamento(num)) {
            System.out.println("El departamento " + num + " no existe.");
            return;
        }
        try {
            // 2 bytes id + 30 nombre + 30 localidad = 62
            fichero.seek((num - 1) * LONGITUD_REGISTRO + 62);
            fichero.writeShort(empleados);
            System.out.println("Empleados actualizados.");
        } catch (IOException e) {
            System.out.println("Error al actualizar empleados.");
        }
    }

    // Actualiza nombre y localidad
    public static void actualizaNombreLocalidad(short num, String nombre, String localidad) {
        if (!existeDepartamento(num)) {
            System.out.println("El departamento " + num + " no existe");
            return;
        }

        nombre = nombre;
        localidad = localidad;

        if (nombre.isEmpty() || nombre.length() > 15 || localidad.isEmpty() || localidad.length() > 15) {
            System.out.println("Texto no valido. No se ha modificado nada.");
            return;
        }

        try {
            fichero.seek((num - 1) * LONGITUD_REGISTRO + 2);
            StringBuffer bufNombre = new StringBuffer(nombre);
            bufNombre.setLength(15);
            fichero.writeChars(bufNombre.toString());
            StringBuffer bufLocalidad = new StringBuffer(localidad);
            bufLocalidad.setLength(15);
            fichero.writeChars(bufLocalidad.toString());
            System.out.println("Nombre y localidad actualizados");
        } catch (IOException e) {
            System.out.println("Error al actualizar.");
        }
    }

    // Lista todos los departamentos activos
    public static void listadoDepartamentos() {
        try {
            long total = fichero.length() / LONGITUD_REGISTRO;
            int contador = 0;

            System.out.println("LISTADO");
            for (long i = 0; i < total; i++) {
                fichero.seek(i * LONGITUD_REGISTRO);
                short id = fichero.readShort();
                String nombre = leerTexto();
                String localidad = leerTexto();
                short emp = fichero.readShort();

                if (id > 0 && id == (i + 1)) {
                    System.out.println(id + " | " + nombre.trim() + " | " + localidad.trim() + " | " + emp);
                    contador++;
                }
            }

            if (contador == 0)
                System.out.println("No hay departamentos activos.");
            else
                System.out.println("Total: " + contador);
        } catch (IOException e) {
            System.out.println("Error al listar.");
        }
    }

    // Muestra todas las posiciones del fichero
    public static void inspeccionFisica() {
        try {
            long total = fichero.length() / LONGITUD_REGISTRO;
            System.out.println("INSPECCION FISICA");

            for (long i = 0; i < total; i++) {
                long desp = i * LONGITUD_REGISTRO;
                fichero.seek(desp);
                short id = fichero.readShort();

                String estado;
                if (id == 0)       estado = "Nunca utilizada";
                else if (id == -1) estado = "Eliminada";
                else if (id == i + 1) estado = "Activa";
                else               estado = "Anomala";

                System.out.println("Pos " + (i+1) + " | Byte " + desp + " | Id esperado: " + (i+1) + " | Id guardado: " + id + " | " + estado);
            }
        } catch (IOException e) {
            System.out.println("Error en la inspeccion.");
        }
    }

    // Estadisticas del fichero
    public static void estadisticas() {
        try {
            long total = fichero.length() / LONGITUD_REGISTRO;
            int activos = 0, eliminados = 0, nuncaUsados = 0, sumaEmp = 0;

            for (long i = 0; i < total; i++) {
                fichero.seek(i * LONGITUD_REGISTRO);
                short id = fichero.readShort();
                leerTexto();
                leerTexto();
                short emp = fichero.readShort();

                if (id > 0 && id == (i + 1)) { activos++; sumaEmp += emp; }
                else if (id == -1) eliminados++;
                else if (id == 0) nuncaUsados++;
            }

            double porc = 0;
            if ((eliminados + nuncaUsados) > 0)
                porc = (double) activos / (eliminados + nuncaUsados) * 100;

            System.out.println("ESTADISTICAS ");
            System.out.println("Longitud: " + fichero.length() + " bytes");
            System.out.println("Activos: " + activos);
            System.out.println("Eliminados: " + eliminados);
            System.out.println("Nunca usados: " + nuncaUsados);
            System.out.println("Total empleados: " + sumaEmp);
            System.out.printf("Ocupacion: %.2f%%%n", porc);
        } catch (IOException e) {
            System.out.println("Error en estadisticas.");
        }
    }

    // Comprueba la integridad del fichero
    public static boolean integridad() {
        boolean ok = true;
        try {
            long longitud = fichero.length();
            if (longitud % LONGITUD_REGISTRO != 0) {
                System.out.println("Casiii, el tamaño del archivo no es múltiplo de 64");
                ok = false;
            }

            long total = longitud / LONGITUD_REGISTRO;
            for (long i = 0; i < total; i++) {
                long desp = i * LONGITUD_REGISTRO;
                fichero.seek(desp);
                short id = fichero.readShort();
                String nombre = leerTexto();
                String localidad = leerTexto();
                short emp = fichero.readShort();

                if (id < -1) {
                    System.out.println("Falló en el byte " + desp + ": ID negativo raro");
                    ok = false;
                } else if (id > 0 && id != (i + 1)) {
                    System.out.println("Falló en el byte " + desp + ": el ID no coincide con la posición");
                    ok = false;
                } else if (id > 0 && emp < 0) {
                    System.out.println("Falló en el byte " + desp + ": hay empleados en negativo");
                    ok = false;
                } else if (id > 0 && nombre.trim().isEmpty()) {
                    System.out.println("Falló en el byte " + desp + ": nombre vacío en activo");
                    ok = false;
                }
            }
        } catch (IOException e) {
            System.out.println("Hubo un problema al leer el archivo para la integridad");
            ok = false;
        }

        if (ok) System.out.println("Locura, el fichero pasa la prueba de integridad");
        return ok;
    }

    // Lee 15 caracteres del fichero
    static String leerTexto() throws IOException {
        char[] c = new char[15];
        for (int i = 0; i < 15; i++)
            c[i] = fichero.readChar();
        return new String(c);
    }
}