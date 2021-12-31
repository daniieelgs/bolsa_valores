# bolsa_valores

Simulador de bolsa pseudo-random. Dónde los inversores pueden hacer mejores inversiones dependiendo de el conocimiento sobre las fluctuaciones que tiene el mercado.

![image](https://i.imgur.com/Oqoujse.png)

En este simulador vas a poder controlar todos los parametros de cada inversor y de cada empresa

![image](https://i.imgur.com/Apej5FR.png) 

## Inicio
* Ejecute 'Bolsa de Valores vX.X.jar' para inicar el software
* **Version mínima del JSE: `1.8`**
> Si no tiene el JVM instalado
* Ejecute 'Bolsa de Valores vX.X.exe' para inicar el software

## Quick Usage
>Inversores
1. En el panel de la izquierda, se le irá mostrando una lista de los inversores configurados con sus respectivos valores. Puede configurar, nombre, dinero inicial y probabilidad de acierto en las inversiones
2. Pulse el boton inferior `Añadir inversor` para añadir un nuevo inversor al panel.
3. Si desea incluir muchos inversores de golpe con valores parecidos, seleccione el check de arriba `Automático`, introduzca el número de inversores que desee y haga click en el tick
>Empresas
1. En el panel de la derecha, se le irá mostrando una lista de las empresas configurados con sus respectivos valores. Puede configurar, nombre, número de acciones disponibles, 
valor inicial de cada acción, valor máximo y mínimo de las acciones, porcentaje de fluctuación después de ser comprada o vendida, y la previsión de la fluctuación de las acciones en cada trimestre del año. **Por defecto será aleatorio hasta que desactive el check `Aleatorio`**
2. Pulsa el boton inferior `Añadir empresa` para añadir una nueva empresa al panel.
3. Si desea incluir muchas empresas de golpe con valores parecidos, seleccione el check de arriba `Automático`, introduzca el número de empresas que desee y haga click en el tick

* En la parte inferior derecha puede configurar el número de ejecciones paralelas que efectuará el simulador. De esta manera podrá hacer varias simulaciones a la vez para obtener unos resultados más exactos
* Para iniciar la simulación haga click en el boton `Calcular`

## Configuración
Haga click en el boton inferior izquierdo del engranaje para abrir la configuración</br>
![image](https://i.imgur.com/nxC3lr3.png)
### Cambiar valores por defecto
Si desea incluir muchos inversores/empresas a la vez con otros valores en la sección `Valores por defecto` podrá modíficar estos valores por defecto
<
### Model
Puede crear su propia configuración, con unos valores por defecto, una lista de inversores por defecto y una lista de empresas por defecto que cargue siempre por defecto al iniciar el programa</br>
Los Modelos le permite exportar una configuración personalizada y compartirla con otros usuarios. De esta forma puede importar Modelos externos o propios para que al cargar el programa siempre inicie con los mismos inversores y empresas
> Guardar Modelo
1. Configure los valores por defecto que desee
2. Añada todos los inversores que quiera con los valores que desee
3. Añada todas las empresas que quiera con los valores que desee
4. Vaya a configuración y a la sección Cargar/Guardar modelo
5. Pulse el icono de la izquierda, seleccione ruta de almacenaje y nombre del archivo **Se recomienda dejar la ruta por defecto**
> Cargar Modelo
1. Vaya a configuración y a la sección Cargar/Guardar modelo
2. Haga click en el boton de la derecha `Cargar`
3. Busque el archivo de modelo que desee importar. **Si es un modelo propio y lo ha dejado en la ruta por defecto, le saldrá directamente**
4. Haga click en el boton inferior `Aplicar`
5. Si desea eliminar el modelo, vaya al mismo sitio y pulse en la cruz roja que esta situado encima del boton de Cargar. Finalmente aplique los cambios

## Resultados
![image](https://i.imgur.com/iQeoVkV.png)
Una vez finalizada las simulaciones se le mostrarán los resultados
* En el panel de la izquierda se le mostrará un resumen de todas las ejecuciones con los valores finales de cada inversor
* En el panel de la derecha podrá abrir los informes con información detallada de cada ejecución
* En el panel central se le mostrará la tabla de resultados finales donde se mostrarán todos los inversores indicando el número de veces que ha quedado 1º, 2º, 3º... Teniendo en cuenta que quedar primero significa ser el inversor con mayor dinero al finalizar y último ser el inversor con menos dinero al finalizar
> **Los informes detallados serán eliminados al realizar una nueva simulación** Si desea almacenarlos dirijase a la ruta del programa y entre a 'Bolsa de Valores/logs/' y mueva todos los archivos en un lugar seguro. Estos contiene la información detalla de cada ejecución. Dirijase a la carpeta 'Bolsa de Valores' y mueva el archivo 'resultados_log.txt' a un lugar seguro. Este contiene la tabla final de resultados. **No podrá visualizar ni mover estos archivos hasta que cierre el programa 'Bolsa de Valores (vX.Y)'**
