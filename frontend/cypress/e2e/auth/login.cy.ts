describe('Login', () => {
  it('permite iniciar sesion con credenciales validas', () => {
    cy.visit('/login');

    cy.intercept('POST', '/auth/login').as('login');

    cy.get('input[formcontrolname="correo"]').type('admin@empleados.local');
    cy.get('input[formcontrolname="contrasena"]').type('password');

    cy.contains('button', 'Entrar').click();

    cy.wait('@login').its('response.statusCode').should('eq', 200);

    // La app redirige a /empleados en login exitoso
    cy.location('pathname', { timeout: 15000 }).should('eq', '/empleados');
  });
});
