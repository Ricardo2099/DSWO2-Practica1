describe('Login', () => {
  it('permite iniciar sesion con credenciales validas', () => {
    cy.visit('/login');

    cy.get('input[formcontrolname="correo"]').type('admin@empleados.local');
    cy.get('input[formcontrolname="contrasena"]').type('password');

    cy.contains('button', 'Entrar').click();

    // La app redirige a /empleados en login exitoso
    cy.location('pathname', { timeout: 15000 }).should('eq', '/empleados');
  });
});
