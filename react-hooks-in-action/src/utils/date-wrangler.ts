import {Week} from "Types/domain";

export function addDays( date: Date, daysToAdd: number): Date {
    const clone = new Date(date.getTime());
    clone.setDate(clone.getDate() + daysToAdd);
    return clone;
}

export function getWeek (forDate: Date, daysOffset: number = 0): Week {
    const date = addDays(forDate, daysOffset);
    // day-of-the-week index of the specific date: Sunday is 0, Monday is 1, ...
    const day = date.getDay();

    return {
        date,
        start: addDays(date, -day),
        end: addDays(date, 6 - day)
    };
}