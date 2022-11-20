# Practica 4: Trabajo en equipo con GitFlow y despliegue en producción.
###### Grupo 5: Benziane Mohammed Adel, Alejandro Company Rincón, Luis Alfonso Culiañez
###### Curso: 2022-2023

## Funcionalidades Nuevas
### 001 Rol de usuario en el equipo

Ahora los usuarios que pertenezcan a un equipo pueden tener dos roles (Lider o miembro). De esta manera el 'lider' sería el encargado de dirigir el equipo
en cuestión.

La premisa para ser lider de un equipo será única y exclusivamente ser el creador del equipo. El resto de usuarios que se unan al mismo adoptarán el rol de
'miembros'.

En el modelo de equipo se ha añadido la propiedad 'lider' de tipo Long, que almacenará el id del usuario que ha creado el equipo en cuestión. Al añadir esta 
propiedad hemos tenido que implementar un nuevo constructor, getter y setter para la misma.

```java
public Equipo(String nombre, String descripcion, Long lider) {
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.lider = lider;
}

public Long getLider() {
        return this.lider;
}

public void setLider(Long lider) {
        this.lider = lider;
}
```

A su vez se ha añadido un nuevo test en `EquipoTest` para comprobar los cambios realizados.

```java
@Test
public void crearEquipoConLider() {
        Usuario usuario = new Usuario("user@ua");
        Equipo equipo = new Equipo("Proyecto P1", "Descripción Proyecto 1", usuario.getId());

        assertThat(equipo.getLider()).isEqualTo(usuario.getId());
}
```

Hemos realizado de igual manera cambios en el servicio de equipos. Ahora pasamos esta nueva propiedad por parámetro y hacemos uso del nuevo constructor para
crear el equipo. Cabe destacar que ahora cuando un usuario crea un equipo se le añade automáticamente y se le proclama lider del mismo.

```java
@Transactional
public Equipo crearEquipo(String nombre, String descripcion, Long lider) {
        if(nombre==""){
        throw new EquipoNoNameException();
        }
        Equipo equipo = new Equipo(nombre,descripcion, lider);
        Usuario usuario = usuarioService.findById(lider);
        equipo.addUsuario(usuario);
        equipoRepository.save(equipo);
        return equipo;
}
```

A raiz de este cambio en el servicio hemos tenido que modificar el controlador de creado de equipos para pasarle el id del usuario que esté creando el equipo en
cuestión.

```java
@PostMapping("/equipos/nuevo")
public String nuevoEquipo(@ModelAttribute EquipoData equipoData, Model model, RedirectAttributes flash, HttpSession session) {
    Long idUsuario = managerUserSession.usuarioLogeado();
    comprobarUsuarioLogeado(idUsuario);
    Usuario usuario = usuarioService.findById(idUsuario);
    equipoService.crearEquipo(equipoData.getNombre(), equipoData.getDescripcion(), usuario.getId());
    flash.addFlashAttribute("mensaje", "Equipo creado correctamente");
    return "redirect:/equipos";
}
```

De igual forma que en el caso del modelo, se han tenido que corregir los test ya implementados añadiendo el nuevo campo al servicio de creación de equipos.

Para el caso de la plantilla hemos modificado en `miembrosEquipo` la plantilla añadiendo una nueva columna para mostrar el rol de cada usuario en el listado de
los mismos en el equipo. Si el usuario es lider se mostrará una 'badge' indicándolo, para el resto será miembro.

```html
<th>Rol</th>
<td>
      <div th:if="${user.id == lider}"><span class="font-weight-bold py-2 px-3 badge badge-pill badge-primary">LIDER</span></div>
      <div th:if="${user.id != lider}"><span class="font-weight-bold py-2 px-3 badge badge-pill badge-secondary">MIEMBRO</span></div>
</td>
}
```

Por último se ha añadido un nuevo test que añade dos usuarios a un equipo y comprueba sus roles, para comprobar que los cambios de la plantilla son correctos.

```java
@Test
public void listaMiembrosConRol()throws Exception{
    //Creamos el usuario 1.
    Usuario us = new Usuario("user@ua");
    us.setPassword("123");
    us.setAdministrador(true);
    us = usuarioService.registrar(us);

    //Creamos el usuario 2.
    Usuario us2 = new Usuario("user2@ua");
    us2.setPassword("123");
    us2 = usuarioService.registrar(us2);

    when(managerUserSession.usuarioLogeado()).thenReturn(us.getId());
    Equipo equipo= equipoService.crearEquipo("Equipo1", "Descripcion Equipo 1", us.getId());
    equipoService.addUsuarioEquipo(us.getId(),equipo.getId());
    equipoService.addUsuarioEquipo(us2.getId(),equipo.getId());
    String url = "/equipos/"+equipo.getId();

    this.mockMvc.perform(get(url))
            .andExpect((content().string(allOf(
                    containsString("user@ua"),
                    containsString("user2@ua"),
                    containsString("LIDER"),
                    containsString("MIEMBRO")
            ))));
}
````