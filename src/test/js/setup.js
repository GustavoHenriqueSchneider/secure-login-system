import '@testing-library/jest-dom';

// Mock Bootstrap
global.bootstrap = {
    Tooltip: jest.fn().mockImplementation(() => ({
        dispose: jest.fn()
    })),
    Alert: jest.fn().mockImplementation(() => ({
        close: jest.fn()
    })),
    Toast: jest.fn().mockImplementation(() => ({
        show: jest.fn()
    }))
};

// Mock console methods to avoid noise in tests
global.console = {
    ...console,
    log: jest.fn(),
    debug: jest.fn(),
    info: jest.fn(),
    warn: jest.fn(),
    error: jest.fn()
};

