describe('Date Formatting', () => {
    test('should format date correctly', () => {
        const testDate = '2024-01-15T10:30:00Z';
        const formatted = formatDate(testDate);
        
        // Verifica se contém elementos esperados da data formatada
        expect(formatted).toMatch(/\d{2}\/\d{2}\/\d{4}/); // DD/MM/YYYY
        expect(formatted).toMatch(/\d{2}:\d{2}/); // HH:MM
    });

    test('should handle invalid date strings', () => {
        expect(() => formatDate('invalid-date')).not.toThrow();
        expect(() => formatDate('')).not.toThrow();
        expect(() => formatDate(null)).not.toThrow();
    });

    test('should handle different date formats', () => {
        const formats = [
            '2024-01-15T10:30:00Z',
            '2024-01-15',
            'January 15, 2024'
        ];

        formats.forEach(dateString => {
            expect(() => formatDate(dateString)).not.toThrow();
        });
    });
});

