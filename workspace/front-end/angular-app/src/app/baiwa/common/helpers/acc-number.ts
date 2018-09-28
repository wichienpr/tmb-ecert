export function convertAccNo(value: string) {
    let str = value.toString();
    return `${sub(str, 0, 3)}-${sub(str, 3, 4)}-${sub(str, 4, 9)}-${sub(str, 9)}`;
}

export function revertAccNo(value: string) {
    let str = value.toString();
    return str.replace(/-/g, '');
}

function sub(value: string, start: number, end?: number) {
    if (end) {
        return value.substring(start, end);
    } else {
        return value.substring(start);
    }
}