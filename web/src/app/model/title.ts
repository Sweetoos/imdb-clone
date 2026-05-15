export interface TitleInfo {
    titleId: number;
    titleType: string;
    primaryTitle: string;
    originalTitle: string;
    isAdult: boolean;
    startYear: number;
    endYear?: number;
    runtimeMinutes: number;
    posterUrl?: string;
}
