# Applets em java demonstrando Tomografia Computadorizada e Reconstrução de Imagens

Desenvolvidos como projeto do curso MT803 "Tópicos em reconstrução de Imagens"

_Prof. Alvaro de Pierro - IMECC - UNICAMP 1997_

## Introdução
A maioria de nós provavelmente já viu ou ouviu falar em tomografia computadorizada ou CT (Computed Tomography)
Uma área multidisciplinar, envolvendo Radiologia, Matemática Aplicada e Computação. 
São imagens geradas por computadores em processo físico semelhante ao raio-X, porém com resolução muito melhor, 
permitindo ao médico um diagnóstico mais preciso. 
A tomografia computadorizada não se aplica somente ao diagnóstico médico, mas a áreas tão diversas como astronomia, 
engenharia, e até detecção de bombas. 
Mas certamente sua principal e mais difundida aplicação é realmente o diagnóstico médico e odontológico. 
Existem outros métodos como MRI (Magnetic Ressonance Imaging - ressonância magnética) e PET (Positron Emission Tomography - tomografia por emissão estimulada de pósitrons), 
mas o CT é o mais amplamente utilizado pelo custo mais acessível e bons resultados em geral.


## Applets

### 1) retroprojeção discreta

Este é o mais simples dos métodos. É possível até deduzi-lo sem a nenhuma matemática avançada, usando apenas argumentos "discretos", mas os resultados são bem ruíns.
A densidade de cada a ponto da imagem é estimada proporcionalmente à soma de todos os valores das integrais de linha das linhas que passam sobre ele, durante o processo de scan. 
Assim, quanto maior forem os valores das integrais que passam sobre ele, mais denso (branco) sua cor na imagem reconstruída.
Experimente mudar o número de vistas e linhas para obter outros resultados.

### 2) método ART

### 3) ART usando wij=1

### 4) método ART (raios divergentes)

### 5) retroprojeção

### 6) Convolução/Retroprojeção


## Bibliografia

Herman, G. T.(1980). "Image Reconstruction from Projections" Academic Press, New York.

## Links

[Atlas do Cerébro](http://www.med.harvard.edu/AANLIB/home.html) - Estudos de doenças do cérebro, com imagens e applets. Aqui as imagens são de Ressonância Magnética e não CT.
