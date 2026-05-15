import { Component, input } from '@angular/core';
import { TitleCardComponent } from "../title-card/title-card";
import { TitleInfo } from '../model/title';

@Component({
  selector: 'app-home',
  imports: [TitleCardComponent],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {
  movie = input.required<TitleInfo>();
  titleInfo: TitleInfo = {
    titleId: 1,
    titleType: 'movie',
    primaryTitle: 'Mroczny Rycerz',
    originalTitle: 'The Dark Knight',
    isAdult: false,
    startYear: 2008,
    runtimeMinutes: 152,
    posterUrl: 'https://via.placeholder.com/150x225/1a1a1a/ffffff?text=Plakat',
  };
}
