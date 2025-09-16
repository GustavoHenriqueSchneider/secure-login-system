describe('Button Loading Functionality', () => {
    let mockButton;

    beforeEach(() => {
        document.body.innerHTML = '<button id="testButton">Click Me</button>';
        mockButton = document.getElementById('testButton');
    });

    afterEach(() => {
        document.body.innerHTML = '';
    });

    test('should show loading state', () => {
        const hideLoading = showButtonLoading(mockButton, 'Loading...');
        
        expect(mockButton.disabled).toBe(true);
        expect(mockButton.innerHTML).toContain('spinner-border');
        expect(mockButton.innerHTML).toContain('Loading...');
        
        hideLoading();
        expect(mockButton.disabled).toBe(false);
        expect(mockButton.innerHTML).toBe('Click Me');
    });

    test('should use default loading text', () => {
        const hideLoading = showButtonLoading(mockButton);
        
        expect(mockButton.innerHTML).toContain('Carregando...');
        
        hideLoading();
    });

    test('should restore original button state', () => {
        const originalHTML = mockButton.innerHTML;
        const hideLoading = showButtonLoading(mockButton, 'Processing...');
        
        hideLoading();
        
        expect(mockButton.innerHTML).toBe(originalHTML);
        expect(mockButton.disabled).toBe(false);
    });
});

