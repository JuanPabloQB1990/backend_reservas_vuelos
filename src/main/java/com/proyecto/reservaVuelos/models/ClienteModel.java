package com.proyecto.reservaVuelos.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "cliente")
@AllArgsConstructor
@NoArgsConstructor
public class ClienteModel implements UserDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idCliente;

    @NotEmpty(message = "nombre requerido")
    private String nombre;

    @NotEmpty(message = "apellido requerido")
    private String apellido;

    @NotEmpty(message = "telefono requerido")
    private String telefono;

    @NotEmpty(message = "correo requerido")
    @Email(message = "formato email no valido")
    private String correo;

    @NotEmpty(message = "username requerido")
    private String username;

    @NotEmpty(message = "password requerido")
    private String password;

    @NotEmpty(message = "pais requerido")
    private String pais;

    @NotEmpty(message = "ciudad requerido")
    private String ciudad;

    @NotEmpty(message = "direccion requerido")
    private String direccion;

    @Enumerated(EnumType.STRING)
    private RolModel rol;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReservacionModel> reservaciones = new ArrayList<>();

    public ClienteModel(Long idCliente, String nombre, String apellido, String telefono, String correo, String username, String password, String pais, String ciudad, String direccion, RolModel rol) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.correo = correo;
        this.username = username;
        this.password = password;
        this.pais = pais;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.rol = rol;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority((rol.name())));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
