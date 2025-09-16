describe('Password Toggle Functionality', () => {
    let mockPasswordField;
    let mockToggleButton;
    let mockIcon;

    beforeEach(() => {
        document.body.innerHTML = `
            <input type="password" id="password" />
            <button id="togglePassword">
                <i class="bi bi-eye"></i>
            </button>
        `;
        
        mockPasswordField = document.getElementById('password');
        mockToggleButton = document.getElementById('togglePassword');
        mockIcon = mockToggleButton.querySelector('i');
    });

    afterEach(() => {
        document.body.innerHTML = '';
    });

    test('should toggle password visibility from password to text', () => {
        // Simula o evento de clique
        mockToggleButton.click();
        
        expect(mockPasswordField.type).toBe('text');
        expect(mockIcon.className).toBe('bi bi-eye-slash');
    });

    test('should toggle password visibility from text to password', () => {
        // Primeiro clique para mostrar senha
        mockToggleButton.click();
        expect(mockPasswordField.type).toBe('text');
        
        // Segundo clique para esconder senha
        mockToggleButton.click();
        expect(mockPasswordField.type).toBe('password');
        expect(mockIcon.className).toBe('bi bi-eye');
    });

    test('should handle missing elements gracefully', () => {
        document.body.innerHTML = '<button id="togglePassword"></button>';
        
        const button = document.getElementById('togglePassword');
        expect(() => button.click()).not.toThrow();
    });
});

