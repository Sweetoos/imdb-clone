import { Routes } from '@angular/router';
import { Home } from './home/home';
import { LoginPanel } from './login-panel/login-panel';

export const routes: Routes = [
  { path: 'home', component: Home },
  { path: 'login-panel', component: LoginPanel },
  { path: "", redirectTo: "/home", pathMatch: "full" }
];
