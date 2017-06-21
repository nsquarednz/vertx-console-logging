import Logging from './Logging.vue';

if (!window.vertxConsoleRoutes) window.vertxConsoleRoutes = [];
window.vertxConsoleRoutes.push({
    path: '/logging',
    component: Logging
});