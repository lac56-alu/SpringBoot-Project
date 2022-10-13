# Practica 2: Aplicación ToDoList
###### Alumno: Benziane Mohammed Adel
###### Profesor: Domingo Gallardo
###### Curso: 2022-2023

## Listado de nuevas clases y métodos implementados.
#### Acerca de
Para la funcionalidad acerca de he añadido una clase homecontroller la cual es un controlador del tyemeleaf about.
#### Barra de menu
Para poder mostrar el menu he añadido unos trozo de codigos de tyemeleaf en cada plantilla donde quiero q se muestre el menu.
Un metodo añadido en Home controller que es ir a tarea para dirigirse desde el menu al listado de tareas de cada usuario logueado.
#### Listado de usuarios registrados
Para mostrar el listado de usuarios registrados he añadido la clase ListadoUsuariosController para devolver el listado y tambien en usuarioService he añadido
un metodo para devolver todos los usuarios registrados de la base de datos.
#### Descripcion de usuario
He añadido la clase descirpcionUsuarioController para mostrar la descripcion entera de usuario con el metodo descripcion de usuario.
#### Usuario admin
Aqui lo unico que he añadido es en la clase usuarioService un metodo hayadministrador para que me devuelva un booleano true en caso de que haya uno y asi no muestro
en la plantilla de registro el checkbox.
#### Proteccion de listado y descripcion de usuario
Para la proteccion de listado y descripcion he añadido en las clases ListadoUsuarioController y DescripcionUsuarioController un metodo para comprobar si un usuario
es admin o no, el metodo es comprobarUsuarioAdmin en caso el usuario no sea admin se lanza una excepcion que es la clase UsuarioNoAdminException.
#### Bloqueo o habiliar usuarios
Para permitir al admin el bloqueo o habiliar a usuarios he añadido metodos en la clase ListadoUsuariosController que son bloquearUsuario y habilitarUsuario 
y en la clase usuarioService tambien bloquearUsuario y habiliarUsuario para modificar si se bloquean o no.
## Listado de plantillas thyemeleaf añadidas.
#### Acerca de
La plantilla about para mostrar los datos del desarrollador.
#### Barra de menu
Añadir en las plantillas listaTareas y about menu. También añadir una nueva plantilla aboutNologueado la cual para usuarios no logueados.
#### Listado de usuarios registrados
Añadir listaUsuarios para mostrar los usuarios registrados y añadir el menu aqui.
#### Descripcion de usuario
También la plantilla descripcionUsuarios para mostrar los datos de usuarios y añadir el menu.
#### Usuario admin
Modificar el formRegistro para mostrar checkbox para registro de admin.
#### Proteccion de listado y descripcion de usuario
Modificar para que la plantilla listaUsuarios y descripcionUsuarios solo se muestra para usuario administrador.
#### Bloqueo o habiliar usuarios
Añadir en la plantilla listaUsuarios una columna más que muestre el acceso del usuario y poder bloquearlo o habilitarselo.
## Explicación de los tests implementados.
Para los tests de la aplicación he añadido como 17 test que comprueben el funcionamiento correcto de la aplicación algunos de estos tests son de web que sirven para
controlar los mensajes que aparecen en el navegador y otros tests para comprobar el funcionamiento de los metodos como por ejemplo los metodos de usuarioService.
## Explicación de código fuente relevante de las nuevas funcionalidades implementadas.
Lo mas importante que destacar es que para realizar el punto usuario admin tenia que modificar la tabla usuarios añadiendo una columna administrador para saber si
el usuario es administrador o no, es de tipo booleano. Y para bloquear también tuve que añadir una columna acceso para saber cuando un usuario intente loguearse
ver si esta bloquearo o no en la tabla usuarios accediendo a la columna acceso y si esta a false es porque esta bloqueado.
#### Acerca de
En esta funcionalidad para mostrar los datos del desarrollador y la fecha de release.
#### Barra de menu
He añadido en todas las paginas una barra de menu para que sea facil el acceso a las diferentes paginas sin necesidad de introducir la url.
#### Listado de usuarios registrados
Para esta funcionalidad de listado he añadido una pagina para que se pueda visualizar los usuarios registrados en la base de datos.
#### Descripcion de usuario
En la pagina de listado de usuarios habiliar un enlace que nos lleva a descripcion del usuario para cada uno registrado.
#### Usuario admin
En la pagina de registro añadido un checkbox para que pueda registrase uno y darle al checkbox y se registra como administrador una vez registrado un admin
ya le deshabilito el checkbox para que no se registro mas admin es decir solo 1 se permite en la aplicación.
#### Proteccion de listado y descripcion de usuario
Añadir la proteccion http para que nadie accede, solo se permite al admin. En caso de un usuario normal le salta exception un error de http tipo no autorizado.
#### Bloqueo o habiliar usuarios
Un usuario administrador puede bloquear o habilitar el acceso al usuario en caso de que un usuario este bloqueado cuando hace login no le permite el acceso mostrandole
un mensaje de error.

## Un ejemplo de codigo fuente interesante.
Según mi punto de vista el mas interesante fue el punto de bloqueo de usuarios y no permitir al usuario bloqueado hacer el login.
Este codigo inferior es para mostrar al usuario administrador en el listado de usuarios un boton de habilitar o bloquear usuarios.
```
<td th:if="${!user.administrador}"><button th:if="${user.acceso}" class="btn btn-danger btn-xs" onmouseover="" style="cursor: pointer;"
                                th:onclick="'bloq(\'/registrados/' + ${user.id} + '\')'">Bloquear</button>
                        <button th:if="${!user.acceso}" class="btn btn-success btn-xs" onmouseover="" style="cursor: pointer;"
                                th:onclick="'habilit(\'/registrados/' + ${user.id} + '\')'">Habilitar</button>
</td>
```
El codigo inferior para no permitir al usuario bloqueado loguearse.
```
else if (loginStatus == UsuarioService.LoginStatus.ERROR_BLOQUEADO){
            model.addAttribute("error","El usuario esta bloqueado");
            return "formLogin";
}
else if(usuario.get().getAcceso()==false){
            return LoginStatus.ERROR_BLOQUEADO;
}
public enum LoginStatus {LOGIN_OK, USER_NOT_FOUND, ERROR_PASSWORD, ERROR_BLOQUEADO}
```
