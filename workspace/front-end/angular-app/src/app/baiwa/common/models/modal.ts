export interface Modal {
    [X: string]: any;
    // Common Modal Component
    modalId?: string;
    class?: string;
    type?: string;
    for?: string;
    // Modal Service
    msg?: string;
    title?: string;
    color?: string;
    success?: boolean;
    size?: string;
    approveMsg?: string;
    rejectMsg?: string;
}