describe('Global SecureLogin Object', () => {
    test('should expose all utility functions', () => {
        expect(typeof window.SecureLogin.showToast).toBe('function');
        expect(typeof window.SecureLogin.confirmAction).toBe('function');
        expect(typeof window.SecureLogin.showButtonLoading).toBe('function');
        expect(typeof window.SecureLogin.formatDate).toBe('function');
        expect(typeof window.SecureLogin.isValidEmail).toBe('function');
        expect(typeof window.SecureLogin.DOMUtils).toBe('object');
        expect(typeof window.SecureLogin.ValidationUtils).toBe('object');
    });

    test('should have DOMUtils with correct methods', () => {
        expect(typeof window.SecureLogin.DOMUtils.createElement).toBe('function');
        expect(typeof window.SecureLogin.DOMUtils.addClassWithAnimation).toBe('function');
        expect(typeof window.SecureLogin.DOMUtils.removeClassWithAnimation).toBe('function');
    });

    test('should have ValidationUtils with correct methods', () => {
        expect(typeof window.SecureLogin.ValidationUtils.validateForm).toBe('function');
        expect(typeof window.SecureLogin.ValidationUtils.clearValidation).toBe('function');
    });
});

