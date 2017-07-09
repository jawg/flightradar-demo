import Vue from 'vue';
import Main from './app/Main.vue';

import './index.scss';

export default new Vue({
  el: '#root',
  render: h => h(Main)
});
