describe('DOM Utils', () => {
    test('should create element with classes and attributes', () => {
        const element = DOMUtils.createElement('div', ['test-class', 'another-class'], {
            'data-test': 'value',
            'id': 'test-element'
        }, 'Test content');
        
        expect(element.tagName).toBe('DIV');
        expect(element.className).toBe('test-class another-class');
        expect(element.getAttribute('data-test')).toBe('value');
        expect(element.getAttribute('id')).toBe('test-element');
        expect(element.innerHTML).toBe('Test content');
    });

    test('should create element with minimal parameters', () => {
        const element = DOMUtils.createElement('span');
        
        expect(element.tagName).toBe('SPAN');
        expect(element.className).toBe('');
        expect(element.innerHTML).toBe('');
    });

    test('should add class with animation', () => {
        document.body.innerHTML = '<div id="testDiv"></div>';
        const element = document.getElementById('testDiv');
        
        DOMUtils.addClassWithAnimation(element, 'animated-class');
        
        expect(element.classList.contains('animated-class')).toBe(true);
        expect(element.style.transition).toBe('all 0.3s ease');
    });

    test('should remove class with animation', () => {
        document.body.innerHTML = '<div id="testDiv" class="existing-class"></div>';
        const element = document.getElementById('testDiv');
        
        DOMUtils.removeClassWithAnimation(element, 'existing-class');
        
        expect(element.classList.contains('existing-class')).toBe(false);
        expect(element.style.transition).toBe('all 0.3s ease');
    });
});

