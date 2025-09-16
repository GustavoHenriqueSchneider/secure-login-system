describe('Form Validation Integration', () => {
    let mockForm;

    beforeEach(() => {
        document.body.innerHTML = `
            <form class="needs-validation" id="testForm">
                <input type="password" id="password" required />
                <input type="password" id="confirmPassword" required />
                <button type="submit">Submit</button>
            </form>
        `;
        mockForm = document.getElementById('testForm');
    });

    afterEach(() => {
        document.body.innerHTML = '';
    });

    test('should prevent form submission when invalid', () => {
        const submitEvent = new Event('submit', { bubbles: true, cancelable: true });
        const preventDefaultSpy = jest.spyOn(submitEvent, 'preventDefault');
        
        mockForm.dispatchEvent(submitEvent);
        
        expect(preventDefaultSpy).toHaveBeenCalled();
        expect(mockForm.classList.contains('was-validated')).toBe(true);
    });

    test('should validate password confirmation in real time', () => {
        const passwordField = document.getElementById('password');
        const confirmPasswordField = document.getElementById('confirmPassword');
        
        passwordField.value = 'password123';
        confirmPasswordField.value = 'different';
        
        const inputEvent = new Event('input', { bubbles: true });
        confirmPasswordField.dispatchEvent(inputEvent);
        
        expect(confirmPasswordField.validationMessage).toBe('As senhas não coincidem');
        
        confirmPasswordField.value = 'password123';
        confirmPasswordField.dispatchEvent(inputEvent);
        
        expect(confirmPasswordField.validationMessage).toBe('');
    });

    test('should handle missing password fields gracefully', () => {
        document.body.innerHTML = '<form class="needs-validation"></form>';
        const form = document.querySelector('form');
        
        expect(() => {
            const submitEvent = new Event('submit', { bubbles: true, cancelable: true });
            form.dispatchEvent(submitEvent);
        }).not.toThrow();
    });
});

