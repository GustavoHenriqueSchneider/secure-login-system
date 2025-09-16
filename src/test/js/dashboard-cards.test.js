describe('Dashboard Cards Animation', () => {
    let mockCard;

    beforeEach(() => {
        document.body.innerHTML = `
            <div class="dashboard-card" style="transform: translateY(0); box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);">
                Card Content
            </div>
        `;
        mockCard = document.querySelector('.dashboard-card');
    });

    afterEach(() => {
        document.body.innerHTML = '';
    });

    test('should apply hover effect on mouseenter', () => {
        const mouseEnterEvent = new Event('mouseenter', { bubbles: true });
        mockCard.dispatchEvent(mouseEnterEvent);
        
        expect(mockCard.style.transform).toBe('translateY(-5px)');
        expect(mockCard.style.boxShadow).toBe('0 0.5rem 1rem rgba(0, 0, 0, 0.15)');
    });

    test('should remove hover effect on mouseleave', () => {
        // Primeiro aplica o efeito hover
        const mouseEnterEvent = new Event('mouseenter', { bubbles: true });
        mockCard.dispatchEvent(mouseEnterEvent);
        
        // Depois remove o efeito
        const mouseLeaveEvent = new Event('mouseleave', { bubbles: true });
        mockCard.dispatchEvent(mouseLeaveEvent);
        
        expect(mockCard.style.transform).toBe('translateY(0)');
        expect(mockCard.style.boxShadow).toBe('0 0.125rem 0.25rem rgba(0, 0, 0, 0.075)');
    });

    test('should handle multiple cards', () => {
        document.body.innerHTML = `
            <div class="dashboard-card">Card 1</div>
            <div class="dashboard-card">Card 2</div>
        `;
        
        const cards = document.querySelectorAll('.dashboard-card');
        
        cards.forEach(card => {
            const mouseEnterEvent = new Event('mouseenter', { bubbles: true });
            card.dispatchEvent(mouseEnterEvent);
            
            expect(card.style.transform).toBe('translateY(-5px)');
        });
    });
});

