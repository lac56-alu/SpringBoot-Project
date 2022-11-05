# Practica 3: Integración con GitHub Actions y TDD
###### Alumno: Benziane Mohammed Adel
###### Profesor: Domingo Gallardo
###### Curso: 2022-2023

## Funcionalidades Nuevas
### 008 Listado de equipos
#### 1.Pantalla de la base de datos
![image](https://user-images.githubusercontent.com/73485527/200116861-1ca44384-e1ec-402c-b1d3-b113a4908308.png)

#### 2.Rutas (endpoints) definidas para las acciones:
La primera ruta es GET /equipos la cual nos lleva a un listado de equipos y dandole a un boton ver miembros nos lleva a GET /equipos/{id} la cual también 
nos lleva al listado de miembros del equipo seleccionado {id}.
##### a.Clases y métodos
Añadido el controlador EquiposController el cual controla las rutas indicadas anteriormente. He añadido también una clase EquipoService para desarrollar
las logicas, ejemplo para devolver el listado de equipos pues primer tengo que ordenar los equipos por sus nombre antes de devolverle la lista al frontend.
##### b.Plantillas thymeleaf
Añadido listaEquipos.html y miembrosEquipo.html para devolver listado de equipos y listado de miembros.
##### c.Tests
La primera clase EquipoServiceTest en la que he añadido tests para comprobar la clase EquipoService es decir comprobar que la logica implementada en la clase
EquipoService funciona correctamente.
La segunda clase EquipoTest sirve para comprobar los equipos si se crean correctamente en la BD.
Por ultimo ListadoEquiposWebTest para comprobar lo que devuelve template html es decir lo que aparece en el frontend.
#### 3.Explicación de algunos fragmentos de código fuente que consideres interesante en las nuevas funcionalidades implementadas.
El codigo inferior lo utilizo para ordenar los equipos por sus nombres me parece interesante como se puede ver primero devuelvo listado de equipos
despues esa lista devuelta que es una lista de Objeto Equipo, la ordeno por sus nombres e1.getNombre().
```
public List<Equipo> findAllOrderedByName(){
        List<Equipo>equipos = equipoRepository.findAll();
        Collections.sort(equipos,
                (Equipo e1,Equipo e2)->e1.getNombre().compareTo(e2.getNombre())
        );
        return equipos;
}
```
### 009 Gestionar pertenencia al equipo
#### 1.Pantalla de la base de datos
![image](https://user-images.githubusercontent.com/73485527/200118023-7af9d0b7-0449-474b-a6b1-6fcc8f8b9c5b.png)

#### 2.Rutas (endpoints) definidas para las acciones:
Las rutas añadidas para esta funcionalidad: POST /equipos/{id} para añadirse al equipo y también DELETE /equipos/{id} para eliminarse del equipo. También
POST /equipos/nuevo que te lleva a un formulario para crear un nuevo equipo. 
##### a.Clases y métodos
En el EquipoController he añadido los metodos para cada ruta
```
    @PostMapping("/equipos/{id}")
    @ResponseBody
    public String meterme(@PathVariable(value="id") Long idE, RedirectAttributes flash, HttpSession session){
        equipoService.addUsuarioEquipo(managerUserSession.usuarioLogeado(),idE);
        return "";
    }
    @DeleteMapping("/equipos/{id}")
    public String eliminarme(@PathVariable(value="id") Long idE, RedirectAttributes flash, HttpSession session){
        equipoService.deleteUsuarioEquipo(managerUserSession.usuarioLogeado(),idE);
        return "";
    }
    @GetMapping("/equipos/nuevo")
    public String formNuevoEquipo(@ModelAttribute EquipoData equipoData, Model model,
                                 HttpSession session) {
        comprobarUsuarioLogeado(managerUserSession.usuarioLogeado());
        return "formNuevoEquipo";
    }
    @PostMapping("/equipos/nuevo")
    public String nuevoEquipo(@ModelAttribute EquipoData equipoData,
                             Model model, RedirectAttributes flash,
                             HttpSession session) {
        comprobarUsuarioLogeado(managerUserSession.usuarioLogeado());
        equipoService.crearEquipo(equipoData.getNombre());
        flash.addFlashAttribute("mensaje", "Equipo creado correctamente");
        return "redirect:/equipos";
    }
```
Y también en EquipoService añadí el codigo necesario para añadir o eliminar el usuario del equipo (addUsuarioEquipo/deleteUsuarioEquipo).
##### b.Plantillas thymeleaf
Editar listaEquipos.html para que aparezca el boton metermer o eliminarme del equipo y añadir un boton para crear equipo que nos lleva al formulario
formNuevoEquipo.html .
##### c.Tests
Primero en la clase EquipoServiceTest añadidos los metodos para añadirse o eliminarse del equipo o crear equipos y asi comprobar que se añaden los usuarios
al equipo y se eliminan perfectamente.
En la clase GestionEquipoWebTest añadido los tests (listaEquipos) para comprobar de que aparezcan los botones de meterme y eliminarme del equipo.
#### 3.Explicación de algunos fragmentos de código fuente que consideres interesante en las nuevas funcionalidades implementadas.

```
<button th:if="${!pertenecer.pertenezcoAlEquipo(usuario.id,equipo.id)}" class="btn btn-success btn-xs" onmouseover="" style="cursor: pointer;"
                                th:onclick="'meterme(\'/equipos/' + ${equipo.id} + '\')'">Añadirme</button>
```
Como se puede observar en el codigo superior añadido un boton para que aparezca si no pertenezco al equipo.
Y en el codigo inferior es el metodo utilizar para saber si pertenezco o no, recorriendo la lista de usuarios del equipo y comprobar el id del usuario.
```
@Transactional(readOnly = true)
    public boolean searchUsuarioEquipo(Long idU,Long idE){
        boolean encontrado=false;
        List<Usuario>users = usuariosEquipo(idE);
        for(int i=0;i<users.size()&&encontrado==false;i++){
            if(users.get(i).getId()==idU){
                encontrado = true;
            }
        }
        return encontrado;
    }
```
### 010 Gestion de equipos (opcional)
#### 1.Pantalla de la base de datos
![image](https://user-images.githubusercontent.com/73485527/200128955-84a7a03b-a775-4d3b-bbdb-b4b4595b3b68.png)
#### 2.Rutas (endpoints) definidas para las acciones:
Las rutas utilizadas para esta funcionalidad POST /equipos/{id}/editar. DELETE /equipos/{id}/eliminar. La primera se utiliza para editar el equipo
que nos lleva al formulario formEditarEquipo.html y la segunda ruta para eliminar el equipo de BD.
##### a.Clases y métodos
Fue añadido los metodos formEditarEquipo y eliminarEquipo en el controlador EquipoController y en el EquipoService modificarEquipo,eliminarEquipo
para modificar el equipo en la BD o eliminarlo.
##### b.Plantillas thymeleaf
En la plantilla listaEquipo.html añadido el acceso como para editar o eliminar el equipo.
##### c.Tests
La clase EquipoServiceTest añadido los metodos eliminar y modificar para comprobar la eliminación y modificación correcta de los equipos.
Luego en la clase GestionEquipoWebTest fue añadidos los metodos editar y borrar equipo para comprobar que funciona correctamente en el frontend, y
que se actualiza la lista de equipos.
#### 3.Explicación de algunos fragmentos de código fuente que consideres interesante en las nuevas funcionalidades implementadas.
Para mi lo interesante de esta funcionalidad es que la acción de editar o borrar equipo solo se aparezca al usuario administrador y que los usuarios
normales no les aparezca esa columna.
Y se puede ver en el template listaEquipos.html fue añadido el codigo inferior que comprueba que el usuario logueado sea administrador.
```
<th th:if="${soyadmin}">Accion</th>
```
## URL DOCKER Y GITHUB:
Docker : https://hub.docker.com/repository/docker/adelbenziane/mads-todolist

Github: https://github.com/mads-ua-22-23/mads-todolist-BenzianeAdel
