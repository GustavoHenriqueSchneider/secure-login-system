describe('Email Validation', () => {
    test('should validate correct email formats', () => {
        const validEmails = [
            'test@example.com',
            'user.name@domain.co.uk',
            'user+tag@example.org',
            '123@test.com'
        ];

        validEmails.forEach(email => {
            expect(isValidEmail(email)).toBe(true);
        });
    });

    test('should reject invalid email formats', () => {
        const invalidEmails = [
            'invalid-email',
            '@example.com',
            'test@',
            'test.example.com',
            '',
            'test@.com',
            'test@example.',
            'test space@example.com'
        ];

        invalidEmails.forEach(email => {
            expect(isValidEmail(email)).toBe(false);
        });
    });

    test('should handle edge cases', () => {
        expect(isValidEmail(null)).toBe(false);
        expect(isValidEmail(undefined)).toBe(false);
        expect(isValidEmail(123)).toBe(false);
    });
});

