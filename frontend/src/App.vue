<template>
  <div class="h-full">
    <Converter/>
    <div class="testSize">
<!--    <CopiableTextArea></CopiableTextArea>-->
    </div>
  </div>
</template>

<script>
import Converter from "./components/Converter";
import CopiableTextArea from "./components/CopiableTextArea";

export default {
  name: 'App',
  components: {
    CopiableTextArea,
    Converter
  }
}
</script>

<style>

html, body {
  height: 100%;
}


.testSize {
  height : 300px
}

</style>
