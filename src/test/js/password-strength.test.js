describe('Password Strength Validation', () => {
    test('should return weak strength for short passwords', () => {
        const result = validatePasswordStrength('123');
        expect(result.strength).toBeLessThan(40);
        expect(result.feedback[0]).toBe('Senha muito fraca');
        expect(result.feedback[1]).toBe('danger');
    });

    test('should return medium strength for passwords with basic requirements', () => {
        const result = validatePasswordStrength('password123');
        expect(result.strength).toBeGreaterThanOrEqual(40);
        expect(result.strength).toBeLessThan(70);
        expect(result.feedback[0]).toBe('Senha média');
        expect(result.feedback[1]).toBe('warning');
    });

    test('should return strong strength for complex passwords', () => {
        const result = validatePasswordStrength('Password123!');
        expect(result.strength).toBeGreaterThanOrEqual(70);
        expect(result.feedback[0]).toBe('Senha forte');
        expect(result.feedback[1]).toBe('success');
    });

    test('should calculate strength correctly for various password patterns', () => {
        // Teste com senha de 6 caracteres
        let result = validatePasswordStrength('123456');
        expect(result.strength).toBe(30); // 20 (length >= 6) + 10 (numbers)

        // Teste com senha de 8 caracteres
        result = validatePasswordStrength('password');
        expect(result.strength).toBe(40); // 20 (length >= 6) + 20 (length >= 8)

        // Teste com senha com maiúsculas e minúsculas
        result = validatePasswordStrength('Password');
        expect(result.strength).toBe(60); // 20 + 20 + 20 (lowercase) + 20 (uppercase)

        // Teste com senha completa
        result = validatePasswordStrength('Password123!');
        expect(result.strength).toBe(100); // Todos os critérios
    });
});

