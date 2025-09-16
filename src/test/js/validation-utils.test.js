describe('Validation Utils', () => {
    let mockForm;

    beforeEach(() => {
        document.body.innerHTML = `
            <form id="testForm">
                <input type="text" id="username" required />
                <input type="email" id="email" required />
                <input type="password" id="password" required />
                <input type="text" id="optional" />
            </form>
        `;
        mockForm = document.getElementById('testForm');
    });

    afterEach(() => {
        document.body.innerHTML = '';
    });

    test('should validate form with all required fields filled', () => {
        document.getElementById('username').value = 'testuser';
        document.getElementById('email').value = 'test@example.com';
        document.getElementById('password').value = 'password123';
        
        const isValid = ValidationUtils.validateForm(mockForm);
        
        expect(isValid).toBe(true);
        expect(document.getElementById('username').classList.contains('is-valid')).toBe(true);
        expect(document.getElementById('email').classList.contains('is-valid')).toBe(true);
        expect(document.getElementById('password').classList.contains('is-valid')).toBe(true);
    });

    test('should invalidate form with empty required fields', () => {
        document.getElementById('username').value = '';
        document.getElementById('email').value = 'test@example.com';
        document.getElementById('password').value = '';
        
        const isValid = ValidationUtils.validateForm(mockForm);
        
        expect(isValid).toBe(false);
        expect(document.getElementById('username').classList.contains('is-invalid')).toBe(true);
        expect(document.getElementById('password').classList.contains('is-invalid')).toBe(true);
    });

    test('should clear validation classes', () => {
        document.getElementById('username').classList.add('is-valid');
        document.getElementById('email').classList.add('is-invalid');
        
        ValidationUtils.clearValidation(mockForm);
        
        expect(document.getElementById('username').classList.contains('is-valid')).toBe(false);
        expect(document.getElementById('email').classList.contains('is-invalid')).toBe(false);
    });

    test('should handle whitespace-only values as invalid', () => {
        document.getElementById('username').value = '   ';
        document.getElementById('email').value = 'test@example.com';
        document.getElementById('password').value = 'password123';
        
        const isValid = ValidationUtils.validateForm(mockForm);
        
        expect(isValid).toBe(false);
        expect(document.getElementById('username').classList.contains('is-invalid')).toBe(true);
    });
});

