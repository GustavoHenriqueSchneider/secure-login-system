describe('Toast Notifications', () => {
    beforeEach(() => {
        document.body.innerHTML = '';
    });

    afterEach(() => {
        document.body.innerHTML = '';
    });

    test('should create toast container if it does not exist', () => {
        showToast('Test message', 'info');
        
        const container = document.getElementById('toast-container');
        expect(container).toBeTruthy();
        expect(container.className).toContain('toast-container');
    });

    test('should create toast with correct message and type', () => {
        showToast('Success message', 'success');
        
        const container = document.getElementById('toast-container');
        const toast = container.querySelector('.toast');
        
        expect(toast).toBeTruthy();
        expect(toast.className).toContain('bg-success');
        expect(toast.innerHTML).toContain('Success message');
    });

    test('should use default type when not specified', () => {
        showToast('Info message');
        
        const container = document.getElementById('toast-container');
        const toast = container.querySelector('.toast');
        
        expect(toast.className).toContain('bg-info');
    });

    test('should create unique toast IDs', () => {
        showToast('First message');
        showToast('Second message');
        
        const container = document.getElementById('toast-container');
        const toasts = container.querySelectorAll('.toast');
        
        expect(toasts).toHaveLength(2);
        expect(toasts[0].id).not.toBe(toasts[1].id);
    });
});

