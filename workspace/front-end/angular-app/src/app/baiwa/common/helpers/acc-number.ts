export function convertAccNo(value: string) {
    let str = value.toString();
    if (str.length == 10)
        return `${sub(str, 0, 3)}-${sub(str, 3, 4)}-${sub(str, 4, 9)}-${sub(str, 9)}`;
    else
        return value;
}

export function revertAccNo(value: string) {
    let str = value.toString();
    if (str.replace(/-/g, '').length == 10)
        return str.replace(/-/g, '');
    else
        return value;
}

function sub(value: string, start: number, end?: number) {
    if (end) {
        return value.substring(start, end);
    } else {
        return value.substring(start);
    }
}