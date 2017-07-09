<template>
  <div class="title">
    <Countdown deadline="July 14, 2017 00:00"></Countdown>
  </div>
</template>

<script>
import Countdown from './Countdown.vue';

export default {
  name: 'Title',
  components: { Countdown }
};
</script>
