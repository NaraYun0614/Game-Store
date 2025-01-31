<template>
  <div v-if="category">
    <h1>{{ category.name }}</h1>
    <p>{{ category.description }}</p>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

export default {
  name: 'CategoryDetails',
  setup() {
    const route = useRoute();
    const category = ref(null);

    onMounted(async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/categories/${route.params.id}`);
        category.value = response.data;
      } catch (error) {
        console.error('Error fetching category details:', error);
      }
    });

    return { category };
  },
};
</script>