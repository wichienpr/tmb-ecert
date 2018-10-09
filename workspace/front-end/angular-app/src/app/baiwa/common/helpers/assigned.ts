export class Assigned {
    
    constructor() {}

    getValue(value: any): any {
        return JSON.parse(JSON.stringify( value ));
    }
}