// Aguarda o carregamento completo da página
document.addEventListener('DOMContentLoaded', function() {
    console.log('Sistema de Login Seguro carregado');
    
    // Inicializa funcionalidades
    initializePasswordToggle();
    initializeFormValidation();
    initializeTooltips();
    initializeAlerts();
    initializeDashboardCards();
});

/**
 * Inicializa o toggle de visibilidade de senha
 */
function initializePasswordToggle() {
    const toggleButtons = document.querySelectorAll('[id^="togglePassword"], [id^="toggleConfirmPassword"]');
    
    toggleButtons.forEach(button => {
        button.addEventListener('click', function() {
            const targetId = this.id.replace('toggle', '').toLowerCase();
            const passwordField = document.getElementById(targetId);
            const icon = this.querySelector('i');
            
            if (passwordField && icon) {
                if (passwordField.type === 'password') {
                    passwordField.type = 'text';
                    icon.className = 'bi bi-eye-slash';
                } else {
                    passwordField.type = 'password';
                    icon.className = 'bi bi-eye';
                }
            }
        });
    });
}

/**
 * Inicializa validação de formulários
 */
function initializeFormValidation() {
    const forms = document.querySelectorAll('.needs-validation');
    
    forms.forEach(form => {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });
    
    // Validação em tempo real para confirmação de senha
    const passwordField = document.getElementById('password');
    const confirmPasswordField = document.getElementById('confirmPassword');
    
    if (passwordField && confirmPasswordField) {
        confirmPasswordField.addEventListener('input', function() {
            if (this.value !== passwordField.value) {
                this.setCustomValidity('As senhas não coincidem');
            } else {
                this.setCustomValidity('');
            }
        });
    }
}

/**
 * Inicializa tooltips do Bootstrap
 */
function initializeTooltips() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

/**
 * Inicializa alertas com auto-dismiss
 */
function initializeAlerts() {
    const alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
    
    alerts.forEach(alert => {
        // Auto-dismiss após 5 segundos
        setTimeout(() => {
            if (alert && alert.parentNode) {
                const bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            }
        }, 5000);
    });
}

/**
 * Inicializa cards do dashboard com efeitos hover
 */
function initializeDashboardCards() {
    const dashboardCards = document.querySelectorAll('.dashboard-card');
    
    dashboardCards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-5px)';
            this.style.boxShadow = '0 0.5rem 1rem rgba(0, 0, 0, 0.15)';
        });
        
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
            this.style.boxShadow = '0 0.125rem 0.25rem rgba(0, 0, 0, 0.075)';
        });
    });
}

/**
 * Valida força da senha
 */
function validatePasswordStrength(password) {
    let strength = 0;
    let feedback = [];
    
    if (password.length >= 6) strength += 20;
    if (password.length >= 8) strength += 20;
    if (/[a-z]/.test(password)) strength += 20;
    if (/[A-Z]/.test(password)) strength += 20;
    if (/[0-9]/.test(password)) strength += 10;
    if (/[^A-Za-z0-9]/.test(password)) strength += 10;
    
    if (strength < 40) {
        feedback = ['Senha muito fraca', 'danger'];
    } else if (strength < 70) {
        feedback = ['Senha média', 'warning'];
    } else {
        feedback = ['Senha forte', 'success'];
    }
    
    return { strength, feedback };
}

/**
 * Atualiza indicador de força da senha
 */
function updatePasswordStrengthIndicator(password) {
    const strengthBar = document.getElementById('passwordStrength');
    const strengthText = document.getElementById('passwordStrengthText');
    
    if (strengthBar && strengthText) {
        const result = validatePasswordStrength(password);
        
        strengthBar.style.width = result.strength + '%';
        strengthBar.className = `progress-bar bg-${result.feedback[1]}`;
        strengthText.textContent = result.feedback[0];
    }
}

/**
 * Formata data para exibição
 */
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

/**
 * Mostra loading em botões
 */
function showButtonLoading(button, text = 'Carregando...') {
    const originalText = button.innerHTML;
    button.innerHTML = `
        <span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
        ${text}
    `;
    button.disabled = true;
    
    return function hideButtonLoading() {
        button.innerHTML = originalText;
        button.disabled = false;
    };
}

/**
 * Confirma ação antes de executar
 */
function confirmAction(message, callback) {
    if (confirm(message)) {
        callback();
    }
}

/**
 * Mostra notificação toast
 */
function showToast(message, type = 'info') {
    const toastContainer = document.getElementById('toast-container') || createToastContainer();
    
    const toastId = 'toast-' + Date.now();
    const toastHtml = `
        <div id="${toastId}" class="toast align-items-center text-white bg-${type} border-0" role="alert">
            <div class="d-flex">
                <div class="toast-body">
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        </div>
    `;
    
    toastContainer.insertAdjacentHTML('beforeend', toastHtml);
    
    const toastElement = document.getElementById(toastId);
    const toast = new bootstrap.Toast(toastElement);
    toast.show();
    
    // Remove o toast do DOM após ser escondido
    toastElement.addEventListener('hidden.bs.toast', function() {
        this.remove();
    });
}

/**
 * Cria container para toasts se não existir
 */
function createToastContainer() {
    const container = document.createElement('div');
    container.id = 'toast-container';
    container.className = 'toast-container position-fixed top-0 end-0 p-3';
    container.style.zIndex = '9999';
    document.body.appendChild(container);
    return container;
}

/**
 * Valida email
 */
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

/**
 * Valida força da senha em tempo real
 */
document.addEventListener('DOMContentLoaded', function() {
    const passwordField = document.getElementById('password');
    if (passwordField) {
        passwordField.addEventListener('input', function() {
            updatePasswordStrengthIndicator(this.value);
        });
    }
});

/**
 * Utilitários para manipulação de DOM
 */
const DOMUtils = {
    /**
     * Cria elemento com classes e atributos
     */
    createElement: function(tag, classes = [], attributes = {}, content = '') {
        const element = document.createElement(tag);
        element.className = classes.join(' ');
        element.innerHTML = content;
        
        Object.keys(attributes).forEach(key => {
            element.setAttribute(key, attributes[key]);
        });
        
        return element;
    },
    
    /**
     * Adiciona classe com animação
     */
    addClassWithAnimation: function(element, className) {
        element.classList.add(className);
        element.style.transition = 'all 0.3s ease';
    },
    
    /**
     * Remove classe com animação
     */
    removeClassWithAnimation: function(element, className) {
        element.style.transition = 'all 0.3s ease';
        element.classList.remove(className);
    }
};

/**
 * Utilitários para validação
 */
const ValidationUtils = {
    /**
     * Valida formulário completo
     */
    validateForm: function(form) {
        const inputs = form.querySelectorAll('input[required], select[required], textarea[required]');
        let isValid = true;
        
        inputs.forEach(input => {
            if (!input.value.trim()) {
                input.classList.add('is-invalid');
                isValid = false;
            } else {
                input.classList.remove('is-invalid');
                input.classList.add('is-valid');
            }
        });
        
        return isValid;
    },
    
    /**
     * Limpa validações do formulário
     */
    clearValidation: function(form) {
        const inputs = form.querySelectorAll('.is-valid, .is-invalid');
        inputs.forEach(input => {
            input.classList.remove('is-valid', 'is-invalid');
        });
    }
};

// Exporta funções para uso global
window.SecureLogin = {
    showToast,
    confirmAction,
    showButtonLoading,
    formatDate,
    isValidEmail,
    DOMUtils,
    ValidationUtils
};
