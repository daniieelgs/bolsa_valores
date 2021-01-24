/*
 
Licencia MIT

Copyright (c) 2021 DANIEL GARCIA SERRANO

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

https://github.com/daniieelgs/bolsa_valores

daniieelgs@gmail.com

*/

package TDR;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class main_TDR_Leo {
	
	private static frame marco;
	
	public static void main(String[] args) {
		
		File source=new File("Bolsa de valores");
		
		if(!source.exists()) {
			
			source.mkdir();
			
		}
		
		File path=new File("Bolsa de valores/logs");
		
		if(path.exists()) {
			
			File[] files=path.listFiles();
			
			for(File file: files) {
				
				File log=new File("Bolsa de valores/logs/" + file.getName());
				
				log.delete();
				
			}
			
		}else {
			path.mkdir();
		}
		
		path=new File("Bolsa de valores/configuration.config");
		
		if(!path.exists()) {
		
			config.createDefaultConfig();
			
		}
		
		path=new File("Bolsa de valores/models/");
		
		if(!path.exists()) {
			path.mkdir();
		}
		
		config.loadConfiguration();
		
		archivo_resultado final_log=new archivo_resultado();
				
		marco=new frame(final_log);						
		
	}
	
	public static void setPosicion(int ejecucion, int numero_inversor, int posicion) {
		
		marco.setPosicion(ejecucion, numero_inversor, posicion);
		
	}
	
}

class config{
	
	private static configuration conf;
	
	private static default_config_inversor defecto_inversor;
	private static default_config_empresa defecto_empresa;
	private static aspect_config aspect;
	private static model_config model;
	
	private static int executations;
	
	private static ObjectOutputStream save_configuration;
	private static ObjectInputStream load_configuration;
	
	private static boolean[] mostrar_warnings;
	
	public static void createDefaultConfig() {
		
		conf=new configuration();
		
		defecto_inversor=new default_config_inversor();
		
		defecto_inversor.setDinero_inversor(5);
		defecto_inversor.setPorcentaje_acierto(50);
		
		conf.setDefectoInversor(defecto_inversor);
		
		defecto_empresa=new default_config_empresa();
		
		defecto_empresa.setN_acciones(100);
		defecto_empresa.setValor_acciones(1);
		defecto_empresa.setValor_max_acciones(100);
		defecto_empresa.setValor_min_acciones(0.5);
		defecto_empresa.setPorcentaje_variacion(20);
		defecto_empresa.setAleatorio(true);
		double[] valor_trimestres= {0.0, 0.0, 0.0, 0.0};
		defecto_empresa.setValor_trimestres(valor_trimestres);
		
		conf.setDefectoEmpresa(defecto_empresa);
		
		aspect=new aspect_config();
		
		aspect.setDefecto(true);
		aspect.setManager(false);
		aspect.setNimbus(false);
		
		conf.setAspect(aspect);
		
		model=new model_config();
		
		model.setPath("");
		
		conf.setModel(model);
		
		executations=10;
		
		conf.setExecutations(executations);
		
		mostrar_warnings=new boolean[5];
		
		for(int i=0; i<mostrar_warnings.length; i++) {
			mostrar_warnings[i]=true;
		}
		
		conf.setMostrar_Warnings(mostrar_warnings);
				
		try {
			save_configuration=new ObjectOutputStream(new FileOutputStream("Bolsa de valores/configuration.config"));
			save_configuration.writeObject(conf);
			save_configuration.close();
		} catch (FileNotFoundException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().toString(), 0);
			e.printStackTrace();
		} catch (IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().toString(), 0);
			e.printStackTrace();
		}
		
	}

	public static default_config_inversor getDefectoInversor() {
		return defecto_inversor;
	}

	public static void setDefectoInversor(default_config_inversor defecto_inversor) {
		config.defecto_inversor = defecto_inversor;
		conf.setDefectoInversor(defecto_inversor);
	}
	
	public static default_config_empresa getDefectoEmpresa() {
		return defecto_empresa;
	}
	
	public static void setDefectoEmpresa(default_config_empresa defecto_empresa) {
		config.defecto_empresa = defecto_empresa;
		conf.setDefectoEmpresa(defecto_empresa);
	}

	public static aspect_config getAspect() {
		return aspect;
	}

	public static void setAspect(aspect_config aspect) {
		config.aspect = aspect;
		conf.setAspect(aspect);
	}

	public static model_config getModel() {
		return model;
	}

	public static void setModel(model_config model) {
		config.model = model;
		conf.setModel(model);
	}
	
	
	public static int getExecutations() {
		return executations;
	}

	public static void setExecutations(int executations) {
		config.executations = executations;
		conf.setExecutations(executations);
	}
	
	public static boolean getMostrar_Warnings(int i) {
		return mostrar_warnings[i];
	}
	
	public static void setMostrar_Warnings(int i, boolean state) {
		mostrar_warnings[i]=state;
		conf.setMostrar_Warnings(mostrar_warnings);
		saveConfiguration();
	}
 
	public static void saveConfiguration() {
		
		try {
			save_configuration=new ObjectOutputStream(new FileOutputStream("Bolsa de valores/configuration.config"));
			save_configuration.writeObject(conf);
			save_configuration.close();
		} catch (FileNotFoundException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().toString(), 0);
			e.printStackTrace();
		} catch (IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().toString(), 0);
			e.printStackTrace();
		}
		
	}
	
	public static void loadConfiguration() {
		
		try {
			
			load_configuration=new ObjectInputStream(new FileInputStream("Bolsa de valores/configuration.config"));
			conf=(configuration) load_configuration.readObject();
			load_configuration.close();
			
			defecto_inversor=conf.getDefectoInversor();
			defecto_empresa=conf.getDefectoEmpresa();
			aspect=conf.getAspect();
			model=conf.getModel();
			
			executations=conf.getExecutations();
			
			mostrar_warnings=conf.getMostrar_Warnings();
						
		} catch (FileNotFoundException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().toString(), 0);
			e.printStackTrace();
		} catch (IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().toString(), 0);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().toString(), 0);
			e.printStackTrace();
		}
		
	}
	
	public static void removeConfigurationFile() {
		
		File path=new File("Bolsa de valores/configuration.config");
		path.delete();
		
	}
	
}

class configuration implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private default_config_inversor defecto_inversor;
	private default_config_empresa defecto_empresa;
	private model_config model;
	private aspect_config aspect;
	private boolean[] mostrar_warnings;
	
	private int executations;
	
	public boolean[] getMostrar_Warnings() {
		return mostrar_warnings;
	}
	public void setMostrar_Warnings(boolean[] _mostrar_warnings) {
		mostrar_warnings=_mostrar_warnings;
	}
	public int getExecutations() {
		return executations;
	}
	public void setExecutations(int executations) {
		this.executations = executations;
	}
	public default_config_inversor getDefectoInversor() {
		return defecto_inversor;
	}
	public void setDefectoInversor(default_config_inversor defecto_inversor) {
		this.defecto_inversor = defecto_inversor;
	}
	public default_config_empresa getDefectoEmpresa() {
		return defecto_empresa;
	}
	public void setDefectoEmpresa(default_config_empresa defecto_empresa) {
		this.defecto_empresa = defecto_empresa;
	}
	public model_config getModel() {
		return model;
	}
	public void setModel(model_config model) {
		this.model = model;
	}
	public aspect_config getAspect() {
		return aspect;
	}
	public void setAspect(aspect_config aspect) {
		this.aspect = aspect;
	}
		
}

class aspect_config implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private boolean defecto, manager, nimbus;

	public boolean isDefecto() {
		return defecto;
	}

	public void setDefecto(boolean defecto) {
		this.defecto = defecto;
	}

	public boolean isManager() {
		return manager;
	}

	public void setManager(boolean manager) {
		this.manager = manager;
	}

	public boolean isNimbus() {
		return nimbus;
	}

	public void setNimbus(boolean nimbus) {
		this.nimbus = nimbus;
	}
	
}

class model_config implements Serializable{

	private static final long serialVersionUID = 1L;
	private String path;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}

class default_config_inversor implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int dinero_inversor, porcentaje_acierto;
	
	public int getDinero_inversor() {
		return dinero_inversor;
	}
	public void setDinero_inversor(int dinero_inversor) {
		this.dinero_inversor = dinero_inversor;
	}
	public int getPorcentaje_acierto() {
		return porcentaje_acierto;
	}
	public void setPorcentaje_acierto(int porcentaje_acierto) {
		this.porcentaje_acierto = porcentaje_acierto;
	}
	
	
}

class default_config_empresa implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int n_acciones, valor_acciones, valor_max_acciones, porcentaje_variacion;
	private boolean aleatorio;
	private double valor_min_acciones;
	private double[] valor_trimestres;
	
	public int getN_acciones() {
		return n_acciones;
	}
	public void setN_acciones(int n_acciones) {
		this.n_acciones = n_acciones;
	}
	public int getValor_acciones() {
		return valor_acciones;
	}
	public void setValor_acciones(int valor_acciones) {
		this.valor_acciones = valor_acciones;
	}
	public int getValor_max_acciones() {
		return valor_max_acciones;
	}
	public void setValor_max_acciones(int valor_max_acciones) {
		this.valor_max_acciones = valor_max_acciones;
	}
	public int getPorcentaje_variacion() {
		return porcentaje_variacion;
	}
	public void setPorcentaje_variacion(int porcentaje_variacion) {
		this.porcentaje_variacion = porcentaje_variacion;
	}
	public boolean isAleatorio() {
		return aleatorio;
	}
	public void setAleatorio(boolean aleatorio) {
		this.aleatorio = aleatorio;
	}
	public double getValor_min_acciones() {
		return valor_min_acciones;
	}
	public void setValor_min_acciones(double valor_min_acciones) {
		this.valor_min_acciones = valor_min_acciones;
	}
	public double[] getValor_trimestres() {
		return valor_trimestres;
	}
	public void setValor_trimestres(double[] valor_trimestres) {
		this.valor_trimestres = valor_trimestres;
	}
	
}

class model_loader{
	
	private static model model;
	private static String name;
	private static ObjectOutputStream model_write;
	private static ObjectInputStream model_read;
	
	public static void newModel(String _name) {
		model=new model();
		name=_name + ".model";
	}
	
	public static void addInversor(String nombre, int dinero, int acierto) {
		model.addInversor(nombre, dinero, acierto);
	}
	
	public static void addEmpresa(String _nombre, int _acciones, int _valor, int _valor_max, double _valor_min, int _variacion, boolean _aleatorio, double[] _valores_trimestres) {
		model.addEmpresa(_nombre, _acciones, _valor, _valor_max, _valor_min, _variacion, _aleatorio, _valores_trimestres);
	}
	
	public static void setEjecuciones(int e) {
		model.setEjecuciones(e);
	}
	
	public static void saveModel() {
		
		try {
			model_write=new ObjectOutputStream(new FileOutputStream(name));
			model_write.writeObject(model);
			model_write.close();
		} catch (IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().toString(), 0);
			e.printStackTrace();
		}
		
		
	}
	
	public static boolean loadModel(String _nombre) {
		
		name=_nombre;
		
		try {
			model_read=new ObjectInputStream(new FileInputStream(name));
			model=(model) model_read.readObject();
			model_read.close();
			return true;
		} catch (IOException | ClassNotFoundException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().toString(), 0);
			e.printStackTrace();
			return false;
		}
		
	}
	
	public static int getEjecuciones() {
		return model.getEjecuciones();
	}
	
	public static int getNum_Inversores() {
		return model.getNum_Inversores();
	}
	
	public static int getNum_Empresas() {
		return model.getNum_Empresas();
	}
	
	public static String getNombre_Inversor(int i) {
		return model.getNombre_Inversor(i);
	}	
	public static int getDinero_Inversor(int i) {
		return model.getDinero_Inversor(i);
	}
	public static int getAcierto_Inversor(int i) {
		return model.getAcierto_Inversor(i);
	}
	
	public static String getNombre_Empresa(int i) {
		return model.getNombre_Empresa(i);
	}
	public static int getAcciones_Empresa(int i) {
		return model.getAcciones_Empresa(i);
	}
	public static int getValorAcciones_Empresa(int i) {
		return model.getValorAcciones_Empresa(i);
	}
	public static int getValorMax_Empresa(int i) {
		return model.getValorMax_Empresa(i);
	}
	public static double getValorMin_Empresa(int i) {
		return model.getValorMin_Empresa(i);
	}
	public static int getVariacion_Empresa(int i) {
		return model.getVariacion_Empresa(i);
	}
	public static boolean getAleatorio_Empresa(int i) {
		return model.getAleatorio_Empresa(i);
	}
	public static double[] getValoresTrimestres_Empresa(int i) {
		return model.getValoresTrimestres_Empresa(i);
	}


	
}

class model implements Serializable{
		
	private static final long serialVersionUID = 1L;
	ArrayList<inversor_model> inversores=new ArrayList<inversor_model>();
	ArrayList<empresa_model> empresas=new ArrayList<empresa_model>();
	
	private int ejecuciones=0;
	
	public void addInversor(String nombre, int dinero, int acierto) {
				
		inversor_model inversor=new inversor_model(nombre, dinero, acierto);
		
		inversores.add(inversor);
		
	}
	
	public void addEmpresa(String _nombre, int _acciones, int _valor, int _valor_max, double _valor_min, int _variacion, boolean _aleatorio, double[] _valores_trimestres) {
		
		empresa_model empresa=new empresa_model(_nombre, _acciones, _valor, _valor_max, _valor_min, _variacion, _aleatorio, _valores_trimestres);
		
		empresas.add(empresa);
		
	}
	
	public void setEjecuciones(int e) {
		ejecuciones=e;
	}
	
	public int getEjecuciones() {
		return ejecuciones;
	}
	
	public int getNum_Inversores() {
		return inversores.size();
	}
	
	public int getNum_Empresas() {
		return empresas.size();
	}
	
	public String getNombre_Inversor(int i) {
		return inversores.get(i).getNombre();
	}	
	public int getDinero_Inversor(int i) {
		return inversores.get(i).getDinero();
	}
	public int getAcierto_Inversor(int i) {
		return inversores.get(i).getAcierto();
	}
	
	public String getNombre_Empresa(int i) {
		return empresas.get(i).getNombre();
	}
	public int getAcciones_Empresa(int i) {
		return empresas.get(i).getAcciones();
	}
	public int getValorAcciones_Empresa(int i) {
		return empresas.get(i).getValor();
	}
	public int getValorMax_Empresa(int i) {
		return empresas.get(i).getValor_max();
	}
	public double getValorMin_Empresa(int i) {
		return empresas.get(i).getValor_min();
	}
	public int getVariacion_Empresa(int i) {
		return empresas.get(i).getVariacion();
	}
	public boolean getAleatorio_Empresa(int i) {
		return empresas.get(i).isAleatorio();
	}
	public double[] getValoresTrimestres_Empresa(int i) {
		return empresas.get(i).getValores_trimestres();
	}


	
	private class inversor_model implements Serializable{
		
		private static final long serialVersionUID = 1L;
		private int dinero, acierto;
		private String nombre;
		
		public inversor_model(String _nombre, int _dinero, int _acierto) {
			nombre=_nombre;
			dinero=_dinero;
			acierto=_acierto;
		}

		public int getDinero() {
			return dinero;
		}

		public int getAcierto() {
			return acierto;
		}

		public String getNombre() {
			return nombre;
		}
		
	}
	
	private class empresa_model implements Serializable{
		
		private static final long serialVersionUID = 1L;
		private int acciones, valor, valor_max, variacion;
		private double valor_min;
		private double[] valores_trimestres;
		private boolean aleatorio;
		private String nombre;
		
		public empresa_model(String _nombre, int _acciones, int _valor, int _valor_max, double _valor_min, int _variacion, boolean _aleatorio, double[] _valores_trimestres) {
			
			nombre=_nombre;
			acciones=_acciones;
			valor=_valor;
			valor_max=_valor_max;
			valor_min=_valor_min;
			variacion=_variacion;
			aleatorio=_aleatorio;
			valores_trimestres=_valores_trimestres;
			
		}

		public int getAcciones() {
			return acciones;
		}

		public int getValor() {
			return valor;
		}

		public int getValor_max() {
			return valor_max;
		}

		public int getVariacion() {
			return variacion;
		}

		public double getValor_min() {
			return valor_min;
		}

		public double[] getValores_trimestres() {
			return valores_trimestres;
		}

		public boolean isAleatorio() {
			return aleatorio;
		}

		public String getNombre() {
			return nombre;
		}
		
	}
	
}

class frame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private panel_principal panel;
	
	public frame(archivo_resultado final_log) {
		
		if(config.getAspect().isManager()) {
			
			try { 
	            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
	        }
	        catch ( Exception e ) {
				Toolkit.getDefaultToolkit().beep();
	            JOptionPane.showMessageDialog(this, "Imposible modificar el tema visual", "Lookandfeel inválido.",
	              JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
	        }
			
		}else if(config.getAspect().isNimbus()) {
			
			try { 
				UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
	        }
	        catch ( Exception e ) {
				Toolkit.getDefaultToolkit().beep();
	            JOptionPane.showMessageDialog(this, "Imposible modificar el tema visual", "Lookandfeel inválido.",
	              JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
	        }
			
		}else if(config.getAspect().isDefecto()){
			try { 
				UIManager.setLookAndFeel(MetalLookAndFeel.class.getName());
	        }
	        catch ( Exception e ) {
				Toolkit.getDefaultToolkit().beep();
	            JOptionPane.showMessageDialog(this, "Imposible modificar el tema visual", "Lookandfeel inválido.",
	              JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
	        }
		}
		
		
		int ancho=Toolkit.getDefaultToolkit().getScreenSize().width;
		int alto=Toolkit.getDefaultToolkit().getScreenSize().height;	
		
		final int x=ancho/4;
		final int y=alto/4;
		final int w=ancho/2;
		final int h=alto/2;
		
		setTitle("Bolsa de valores (v4.3.0)");
		
		setBounds(x, y, w, h);
		setResizable(false);
				
		URL icon_path=main_TDR_Leo.class.getResource("/TDR/Images/icono.png");	
		Image Icon=Toolkit.getDefaultToolkit().getImage(icon_path);	
		setIconImage(Icon);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel=new panel_principal(w, h, final_log, this);
		
		add(panel);
		
		this.addWindowListener(new WindowAdapter() {
			
			public void windowClosing(WindowEvent e) {
				
				final_log.close();
				
			}
			
		});
				
		setVisible(true);
		
	}
	
	public void setPosicion(int ejecucion, int numero_inversor, int posicion) {
		
		panel.setPosicion(ejecucion, numero_inversor, posicion);
		
	}
	
}

class panel_principal extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private int num_inversores, num_empresas;
	private int [] [] posiciones;
	private JSpinner Sejecuciones;
	
	private inversor_panel inversor;
	private empresa_panel empresa;
	
	int width, height;
	
	public panel_principal(int _width, int _height, archivo_resultado final_log, JFrame marco) {
		
		setLayout(null);
		
		width=_width;
		height=_height;
		
		setBackground(Color.WHITE);
		
		final int inversorX=20;
		final int inversorY=30;
		final int inversorW=width/3 + 60;
		final int inversorH=height-130;
		
		inversor=new inversor_panel(inversorW, inversorH);
				
		inversor.setBounds(inversorX, inversorY, inversorW, inversorH);
		
		JScrollPane scroll_inversor=new JScrollPane(inversor);
		
		scroll_inversor.getVerticalScrollBar().setUnitIncrement(20);
		scroll_inversor.getHorizontalScrollBar().setUnitIncrement(15);
		
		scroll_inversor.setBorder(null);
		
		scroll_inversor.setBounds(inversorX, inversorY, inversorW, inversorH);
		
		add(scroll_inversor);
		
		final int empresaW=width/2;
		final int empresaH=height-130;
		final int empresaX=width-20-empresaW;
		final int empresaY=inversorY;
		
		empresa=new empresa_panel(empresaW, empresaH);
				
		empresa.setBounds(empresaX, empresaY, empresaW, empresaH);
		
		JScrollPane scroll_empresa=new JScrollPane(empresa);
		
		scroll_empresa.setBorder(null);
		
		scroll_empresa.setBounds(empresaX, empresaY, empresaW, empresaH);
		
		add(scroll_empresa);
		
		JLabel Lejecuciones=new JLabel("Nº de ejecuciones: ");
		Sejecuciones=new JSpinner();
		
		Lejecuciones.setToolTipText("Número de ejecuciones");
		
		Lejecuciones.setFont(new Font("Arial", Font.BOLD, 15));
		
		SpinnerNumberModel model_num_ejecuciones=new SpinnerNumberModel(config.getExecutations(), 1, 10000, 1);
		Sejecuciones.setModel(model_num_ejecuciones);
		((JSpinner.DefaultEditor) Sejecuciones.getEditor()).getTextField().setBorder(null);
		Sejecuciones.setBorder(null);
		
		final int calcularW=100;
		final int calcularH=30;
		final int calcularX=width-130;
		final int calcularY=height-90;
		
		
		final int SejecucionW=60;
		final int LejecucionW=140;
		
		final int space=0;
		
		final int SejecucionH=30;
		final int LejecucionH=30;
		
		final int SejecucionX=calcularX - 10 - SejecucionW;
		final int LejecucionX=SejecucionX - space - LejecucionW;


		int SejecucionY=calcularY;
		int LejecucionY=SejecucionY;
		
		Sejecuciones.setBounds(SejecucionX, SejecucionY, SejecucionW, SejecucionH);
		Lejecuciones.setBounds(LejecucionX, LejecucionY, LejecucionW, LejecucionH);
		
		add(Sejecuciones);
		add(Lejecuciones);
		
		if(!(config.getModel().getPath().equals(""))) {
									
			if(model_loader.loadModel(config.getModel().getPath())) {
				inversor.quitAllInversor();
				
				for(int i=0; i<model_loader.getNum_Inversores(); i++) {
									
					inversor.nuevo_inversor_panel(model_loader.getNombre_Inversor(i), model_loader.getDinero_Inversor(i), model_loader.getAcierto_Inversor(i));
					
				}
				
				empresa.quitAllEmpresa();
				
				for(int i=0; i<model_loader.getNum_Empresas(); i++) {
									
					empresa.nueva_empresa_panel(model_loader.getNombre_Empresa(i), model_loader.getAcciones_Empresa(i), model_loader.getValorAcciones_Empresa(i), model_loader.getValorMax_Empresa(i), model_loader.getValorMin_Empresa(i), model_loader.getVariacion_Empresa(i), model_loader.getAleatorio_Empresa(i), model_loader.getValoresTrimestres_Empresa(i));
					
				}
				
				Sejecuciones.setValue(model_loader.getEjecuciones());
			}
			
		}
		
		JButton conf=new JButton("");		
		conf.setOpaque(false);
		conf.setFocusPainted(false);
		conf.setBorderPainted(false); 
		conf.setContentAreaFilled(false); 
		conf.setToolTipText("Configuración");
		
		conf.setIcon(new ImageIcon(main_TDR_Leo.class.getResource("/TDR/Images/conf.png")));
		
		conf.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		conf.addMouseListener(new MouseAdapter() {
			
			public void mouseEntered(MouseEvent e) {
				
				conf.setIcon(new ImageIcon(main_TDR_Leo.class.getResource("/TDR/Images/conf_select.png")));
				
			}
			
			public void mouseExited(MouseEvent e) {
				
				conf.setIcon(new ImageIcon(main_TDR_Leo.class.getResource("/TDR/Images/conf.png")));
				
			}
			
		});
		
		conf.setBounds(10, height-75, 24, 24);
		
		add(conf);
		
		conf.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				int _width=Toolkit.getDefaultToolkit().getScreenSize().width;
				int _height=Toolkit.getDefaultToolkit().getScreenSize().height;
				
				JDialog conf=new JDialog();
				
				URL icon_path=main_TDR_Leo.class.getResource("/TDR/Images/conf.png");	
				Image Icon=Toolkit.getDefaultToolkit().getImage(icon_path);	
				conf.setIconImage(Icon);
				
				conf.setTitle("Configuracion");
				
				JScrollPane scroll=new JScrollPane(new configuracion(conf, marco));
				scroll.getVerticalScrollBar().setUnitIncrement(15);
				scroll.getHorizontalScrollBar().setUnitIncrement(10);
				
				conf.add(scroll);
				
				conf.setBounds(_width/2-_width/8,_height/2-_height/4,_width/4, _height/2);
				
				conf.setModal(true);
				
				conf.setVisible(true);
				
			}
		});
		
		JButton calcular=new JButton("Calcular");
		
		calcular.setCursor(new Cursor(Cursor.HAND_CURSOR));
		Sejecuciones.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		calcular.setBounds(calcularX, calcularY, calcularW, calcularH);
		
		calcular.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
								
				num_inversores=inversor.getNum_inversores();
				num_empresas=empresa.getNum_empresas();
				
				String[] nombre_inversores=new String[num_inversores];
				int[] porcentaje_aciertos=new int[num_inversores];
				int[] dinero_inversores=new int[num_inversores];
				
				for(int i=0; i<num_inversores; i++) {
					
					nombre_inversores[i]=inversor.getNombre(i).toUpperCase();
					porcentaje_aciertos[i]=inversor.getPorcentaje(i);
					dinero_inversores[i]=inversor.getDinero(i);
					
				}
				
				String[] nombre_empresas=new String[num_empresas];
				int[] num_acciones=new int[num_empresas];
				double[] valor_acciones=new double[num_empresas];
				int[] valor_max_acciones=new int[num_empresas];
				double[] valor_min_acciones=new double[num_empresas];
				int[] porcentaje_variacion=new int[num_empresas];
				boolean[] prevision_aleatoria=new boolean[num_empresas];
				double[][] prevision_acciones=new double[num_empresas][4];
				
				for(int i=0; i<num_empresas; i++) {
					
					nombre_empresas[i]=empresa.getNombre(i).toUpperCase();
					num_acciones[i]=empresa.getNum_acciones(i);
					valor_acciones[i]=empresa.getValor_acciones(i);
					valor_max_acciones[i]=empresa.getValor_max_acciones(i);
					valor_min_acciones[i]=empresa.getValor_min_acciones(i);
					porcentaje_variacion[i]=empresa.getPorcentaje_variacion(i);
					prevision_aleatoria[i]=empresa.getPrevision_aleatorio(i);
					prevision_acciones[i]=empresa.getPrevision_acciones(i);
					
				}
						
				ArrayList<String> nombres_in=new ArrayList<String>();
				ArrayList<String> nombres_em=new ArrayList<String>();
				
				boolean repeat=false;
				boolean blank=true;
				
				String listado="No se admiten nombres repetidos\n";
				
				for(int i=0; i<nombre_inversores.length; i++) {
					
					String nombre_in=nombre_inversores[i];
					
					for(int j=0; j<nombre_in.length(); j++) {
						
						char c=nombre_in.charAt(j);
						
						String letras_min="qwertyuiopasdfghjklñzxcvbnm";
						String letras_may="QWERTYUIOPASDFGHJKLÑZXCVBNM";
						String nums="1234567890";
						
						for(int h=0; h<letras_min.length(); h++) {
							
							if(c==letras_min.charAt(h)) {
								blank=false;
								break;
							}
							
						}
						
						for(int h=0; h<letras_may.length(); h++) {
							
							if(c==letras_may.charAt(h)) {
								blank=false;
								break;
							}
							
						}
						
						for(int h=0; h<nums.length(); h++) {
							
							if(c==nums.charAt(h)) {
								blank=false;
								break;
							}
							
						}
						
					}
					
					if(blank) {break;}
					
					for(int j=0; j<nombre_inversores.length; j++) {
						
						String _nombre_in=nombre_inversores[j];
						
						if(nombre_in.equals(_nombre_in)&&i!=j) {
							
							boolean en_lista=false;
							
							for(String s: nombres_in) {
								
								if(s.equals(nombre_in)) {
									en_lista=true;
								}
								
							}
							
							if(!en_lista) {
								nombres_in.add(nombre_in);
								listado=listado + "\nInversor [" + nombre_in + "]";
								repeat=true;
							}

						}
						
					}
					
				}
				
				if(!blank) {
				
				blank=true;
					
				for(int i=0; i<nombre_empresas.length; i++) {
					
					String nombre_em=nombre_empresas[i];
					
					for(int j=0; j<nombre_em.length(); j++) {
						
						char c=nombre_em.charAt(j);
						
						String letras_min="qwertyuiopasdfghjklñzxcvbnm";
						String letras_may="QWERTYUIOPASDFGHJKLÑZXCVBNM";
						String nums="1234567890";
						
						for(int h=0; h<letras_min.length(); h++) {
							
							if(c==letras_min.charAt(h)) {
								blank=false;
								break;
							}
							
						}
						
						for(int h=0; h<letras_may.length(); h++) {
							
							if(c==letras_may.charAt(h)) {
								blank=false;
								break;
							}
							
						}
						
						for(int h=0; h<nums.length(); h++) {
							
							if(c==nums.charAt(h)) {
								blank=false;
								break;
							}
							
						}
						
					}
					
					if(blank) {break;}
					
					for(int j=0; j<nombre_empresas.length; j++) {
						
						String _nombre_em=nombre_empresas[j];
						
						if(nombre_em.equals(_nombre_em)&&i!=j) {
							
							boolean en_lista=false;
							
							for(String s: nombres_em) {
								
								if(s.equals(nombre_em)) {
									en_lista=true;
								}
								
							}
							
							if(!en_lista) {
								nombres_em.add(nombre_em);
								listado=listado + "\nEmpresa [" + nombre_em + "]";
								repeat=true;
							}

						}
						
					}
					
				}
				
				}
				
				if(repeat) {
					
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, listado, "Confliction", 0);
					
				}else if(blank){
					
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, "No se admiten nombres en blanco", "Blank name", 0);
							
				}else {
				
					remove(scroll_inversor);
					remove(scroll_empresa);
					remove(Lejecuciones);
					remove(Sejecuciones);
					remove(calcular);
					remove(conf);
					
					setLayout(new BorderLayout());
					
					JPanel calcular_panel=new calculando();
					
					add(calcular_panel);
					
					updateUI();
					
					Thread calcular_thread=new Thread(new Runnable() {
						
						public void run() {
	
							calcular(final_log, (int) Sejecuciones.getValue(), nombre_inversores, dinero_inversores, porcentaje_aciertos, num_empresas, nombre_empresas, num_acciones, valor_acciones, valor_max_acciones, valor_min_acciones, porcentaje_variacion, prevision_aleatoria, prevision_acciones);
							
							remove(calcular_panel);
							
							JPanel resultados_panel=new resultados(final_log, nombre_inversores);
							
							JPanel resumen_panel=new resumen(final_log);
							
							JPanel informe_panel=new informe ((int) Sejecuciones.getValue());
							
							JButton reboot=new JButton("Reboot");
							reboot.setOpaque(true);
							reboot.setFocusPainted(true);
							reboot.setBorderPainted(true); 
							reboot.setContentAreaFilled(true); 
							reboot.setToolTipText("Reiniciar");
							
							reboot.setBackground(null);
							reboot.setForeground(Color.BLACK);
							reboot.setFont(new Font("Verdana", Font.PLAIN, 12));
							
							reboot.addMouseListener(new MouseAdapter() {
								
								public void mouseEntered(MouseEvent e) {
									
									reboot.setBackground(new Color(225, 238, 238));
									reboot.setForeground(Color.RED);
									reboot.setFont(new Font("Verdana", Font.BOLD, 15));
									
								}
								
								public void mouseExited(MouseEvent e) {
									
									reboot.setBackground(null);
									reboot.setForeground(Color.BLACK);
									reboot.setFont(new Font("Verdana", Font.PLAIN, 12));
									
								}
								
							});
							
							reboot.setCursor(new Cursor(Cursor.HAND_CURSOR));
							
							JScrollPane scroll=new JScrollPane(resultados_panel);
							
							scroll.getVerticalScrollBar().setUnitIncrement(20);
							scroll.getHorizontalScrollBar().setUnitIncrement(15);
							
							add(scroll, BorderLayout.CENTER);
							add(resumen_panel, BorderLayout.WEST);
							add(informe_panel, BorderLayout.EAST);
							add(reboot, BorderLayout.SOUTH);
							
							reboot.addActionListener(new ActionListener() {
								
								public void actionPerformed(ActionEvent e) {
									marco.dispose();
									main_TDR_Leo.main(null);
									
								}
							});
							
							updateUI();
						}
					});
									
					calcular_thread.start();
				
				}
				
			}
		});
		
		add(calcular);
				
		updateUI();
		
		
	}
	
	public void calcular(archivo_resultado final_log, int num, String[] nombre_inversores, int[] dinero_inversores, int[] porcentaje_aciertos, int num_empresas, String[] nombre_empresas, int[] num_acciones, double[] valor_acciones, int[] valor_max_acciones, double[] valor_min_acciones, int[] porcentaje_variacion, boolean[] prevision_aleatoria, double[][] prevision_acciones) {
		
		posiciones=new int[num][num_inversores];

		for(int i=0; i<posiciones.length; i++) {
			
			for(int j=0; j<posiciones[i].length; j++) {
				
				posiciones[i][j]=-1;
				
			}
			
		}
		
		Thread t=null;
		
		for(int i=0; i<num; i++) { //CADA VUELTA NUEVA EJECUCION PARALELA
						
			Runnable r=new start(new archivo(i), final_log, i, num_inversores, nombre_inversores, dinero_inversores, porcentaje_aciertos, num_empresas, nombre_empresas, num_acciones, valor_acciones, valor_max_acciones, valor_min_acciones, porcentaje_variacion, prevision_aleatoria, prevision_acciones);
						
			t=new Thread(r);
			
			final_log.new_thread(t);
			
			t.start();
			
		}
		
		try {
			t.join();
		} catch (InterruptedException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().toString(), 0);
			e.printStackTrace();
		}
		
		final_log.posiciones(num_inversores, nombre_inversores, posiciones);
		
		final_log.firma();
						
	}
	
	public void setPosicion(int ejecucion, int numero_inversor, int posicion) {
		
		posiciones[ejecucion][posicion]=numero_inversor;
		
	}
	
	private class configuracion extends JPanel{

		private static final long serialVersionUID = 1L;

		public configuracion(JDialog conf, JFrame marco) {
			
			setLayout(new BorderLayout());
			
			Box contenido=Box.createVerticalBox();
			
			Box caja_defecto=Box.createVerticalBox();
			
			JPanel defecto=new JPanel();
			
			JPanel inversor_defecto=new inversor_config();
			inversor_defecto.setBorder(new TitledBorder("Inversor"));
			
			JPanel empresa_defecto=new empresa_config();
			empresa_defecto.setBorder(new TitledBorder("Empresa"));
			
			defecto.add(inversor_defecto);
			defecto.add(empresa_defecto);
			
			caja_defecto.setBorder(new TitledBorder("Variables por defecto"));
			
			caja_defecto.add(defecto);
			
			JPanel ejecuciones=new JPanel();
						
			JLabel lejecuciones=new JLabel("Nº de ejecuciones:");
			lejecuciones.setToolTipText("Número de ejecuciones");
			JSpinner sejecuciones=new JSpinner();
			sejecuciones.setModel(new SpinnerNumberModel(config.getExecutations(), 1, 10000, 1));
			
			ejecuciones.add(lejecuciones);
			ejecuciones.add(sejecuciones);
			
			caja_defecto.add(ejecuciones);
			
			contenido.add(caja_defecto);
			
			JPanel modelo=new modelo();
			modelo.setBorder(new TitledBorder("Cargar/Guardar modelo"));
			
			contenido.add(modelo);
			
			JPanel aspecto=new aspecto_config();
			
			aspecto.setBorder(new TitledBorder("Aspecto"));
			
			contenido.add(aspecto);
						
			
			JPanel botones=new JPanel(new GridLayout(1, 3));
			
			JButton cancelar=new JButton("Cancelar");
			JButton restore=new JButton("Restablecer");
			JButton aplicar=new JButton("Aplicar");
			
			cancelar.setToolTipText("Salir sin guardar");
			restore.setToolTipText("Restablecer la configuración por defecto");
			aplicar.setToolTipText("Guardar cambios");
			
			botones.add(cancelar);
			botones.add(restore);
			botones.add(aplicar);
			
			cancelar.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {

					conf.dispose();
					
				}
			});
			
			restore.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
										
					int aceptar=JOptionPane.showConfirmDialog(null, "Se restablecerá toda la configuración por defecto\n¿Quiere continuar?", "Warning", JOptionPane.YES_NO_OPTION);;
										
					if(aceptar==0) {
					
						config.removeConfigurationFile();
						conf.dispose();
						marco.dispose();
						main_TDR_Leo.main(null);
					}
				}
			});
			
			aplicar.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					mensaje m=new mensaje("El sistema se reiniciará para aplicar los cambios", "¿Quiere continuar?", "Warning", 3);
					
					int aceptar=m.show();
										
					if(aceptar==0) {
					
						model_config model=new model_config();
						
						model.setPath(((TDR.panel_principal.configuracion.modelo) modelo).getPath());
						
						config.setModel(model);
						
						aspect_config aspect=new aspect_config();
						
						aspect.setDefecto(((aspecto_config) aspecto).isDefecto());
						aspect.setManager(((aspecto_config) aspecto).isManager());
						aspect.setNimbus(((aspecto_config) aspecto).isNimbus());
						
						config.setAspect(aspect);
						
						default_config_inversor conf_inversor=new default_config_inversor();
						
						conf_inversor.setDinero_inversor(((TDR.panel_principal.configuracion.inversor_config) inversor_defecto).getDinero());
						conf_inversor.setPorcentaje_acierto(((TDR.panel_principal.configuracion.inversor_config) inversor_defecto).getPorcentajeAcierto());
						
						config.setDefectoInversor(conf_inversor);
						
						default_config_empresa conf_empresa=new default_config_empresa();
						
						conf_empresa.setN_acciones(((TDR.panel_principal.configuracion.empresa_config) empresa_defecto).getN_Acciones());
						conf_empresa.setValor_acciones(((TDR.panel_principal.configuracion.empresa_config) empresa_defecto).getValor_Acciones());
						conf_empresa.setValor_max_acciones(((TDR.panel_principal.configuracion.empresa_config) empresa_defecto).getValor_Max_Acciones());
						conf_empresa.setValor_min_acciones(((TDR.panel_principal.configuracion.empresa_config) empresa_defecto).getValor_Min_Acciones());
						conf_empresa.setPorcentaje_variacion(((TDR.panel_principal.configuracion.empresa_config) empresa_defecto).getPorcentaje_Variacion());
						conf_empresa.setAleatorio(((TDR.panel_principal.configuracion.empresa_config) empresa_defecto).isAleatorio());
						conf_empresa.setValor_trimestres(((TDR.panel_principal.configuracion.empresa_config) empresa_defecto).getValor_Trimestres());
						
						config.setDefectoEmpresa(conf_empresa);
						
						config.setExecutations((int) sejecuciones.getValue());
						
						config.saveConfiguration();
						
						conf.dispose();
						marco.dispose();
						main_TDR_Leo.main(null);
					}	
				}
			});
			
			add(contenido, BorderLayout.CENTER);
			add(botones, BorderLayout.SOUTH);
			
		}
		
		private class modelo extends JPanel{

			private static final long serialVersionUID = 1L;
			private JButton guardar, cargar;
			private JTextField source;
			
			public modelo() {
				
				setLayout(new GridLayout(1, 2));
				
				guardar=new JButton("");
				guardar.setOpaque(false);
				guardar.setFocusPainted(false);
				guardar.setBorderPainted(false); 
				guardar.setContentAreaFilled(false); 
				guardar.setIcon(new ImageIcon(main_TDR_Leo.class.getResource("/TDR/Images/guardar.png")));
				guardar.setToolTipText("Guardar modelo actual");
				
				guardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
				
				guardar.addMouseListener(new MouseAdapter() {
					
					public void mouseEntered(MouseEvent e) {
						
						guardar.setIcon(new ImageIcon(main_TDR_Leo.class.getResource("/TDR/Images/guardar_select.png")));
						
					}
					
					public void mouseExited(MouseEvent e) {
						
						guardar.setIcon(new ImageIcon(main_TDR_Leo.class.getResource("/TDR/Images/guardar.png")));
						
					}
					
				});
				
				guardar.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						
						JFileChooser file=new JFileChooser();
						
						file.setCurrentDirectory(new File("Bolsa de valores/models/"));
						
						FileNameExtensionFilter filtro=new FileNameExtensionFilter("Models", "model");
						file.setFileFilter(filtro);
						
						int seleccion=file.showSaveDialog(null);
						
						if(seleccion==JFileChooser.APPROVE_OPTION) {
							File modal_file=file.getSelectedFile();
							
							source.setText(modal_file.getAbsolutePath() + ".model");
						
							model_loader.newModel(modal_file.getAbsolutePath());
							
							for(int i=0; i<inversor.getNum_inversores(); i++) {
								
								model_loader.addInversor(inversor.getNombre(i), inversor.getDinero(i), inversor.getPorcentaje(i));
								
							}
							
							for(int i=0; i<empresa.getNum_empresas(); i++) {
								
								model_loader.addEmpresa(empresa.getNombre(i), empresa.getNum_acciones(i), (int) empresa.getValor_acciones(i), empresa.getValor_max_acciones(i), empresa.getValor_min_acciones(i), empresa.getPorcentaje_variacion(i), empresa.getPrevision_aleatorio(i), empresa.getPrevision_acciones(i));
								
							}
							
							model_loader.setEjecuciones((int) Sejecuciones.getValue());
							
							model_loader.saveModel();
						}
												
					}
				});
				
				add(guardar);
				
				JPanel carga=new JPanel(new GridLayout(2,1));
				
				Box h=Box.createHorizontalBox();
				
				model_config modelo=config.getModel();
								
				source=new JTextField(modelo.getPath());
				cargar=new JButton("Cargar");
				cargar.setToolTipText("Cargar modelo nuevo");
				
				cargar.setCursor(new Cursor(Cursor.HAND_CURSOR));
				
				JButton quitar=new JButton("");
				quitar.setOpaque(false);
				quitar.setFocusPainted(false);
				quitar.setBorderPainted(false); 
				quitar.setContentAreaFilled(false); 
				quitar.setToolTipText("Quitar modelo precargado");
				
				quitar.setIcon(new ImageIcon(main_TDR_Leo.class.getResource("/TDR/Images/eliminar.png")));
				
				quitar.setCursor(new Cursor(Cursor.HAND_CURSOR));
				
				quitar.addMouseListener(new MouseAdapter() {
					
					public void mouseEntered(MouseEvent e) {
						
						quitar.setIcon(new ImageIcon(main_TDR_Leo.class.getResource("/TDR/Images/eliminar_select.png")));
						
					}
					
					public void mouseExited(MouseEvent e) {
						
						quitar.setIcon(new ImageIcon(main_TDR_Leo.class.getResource("/TDR/Images/eliminar.png")));
						
					}
					
				});
				
				h.add(source);
				source.setPreferredSize(source.getSize());
				h.add(quitar);
				
				carga.add(h);
				carga.add(cargar);
				
				quitar.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						
						source.setText("");
						
					}
				});
				
				cargar.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {

						JFileChooser file=new JFileChooser();
						
						file.setCurrentDirectory(new File("Bolsa de valores/models/"));
						
						FileNameExtensionFilter filtro=new FileNameExtensionFilter("Models", "model");
						file.setFileFilter(filtro);
						
						int seleccion=file.showOpenDialog(null);
						
						if(seleccion==JFileChooser.APPROVE_OPTION) {
							
							source.setText(file.getSelectedFile().getAbsolutePath());
							
						}
						
					}
					
				});
				
				add(carga);
				
			}
			
			public String getPath() {
				
				return source.getText();
				
			}
		}
		
		private class aspecto_config extends JPanel{
			
			private static final long serialVersionUID = 1L;
			private JRadioButton defecto, manager, nimbus;
			private ButtonGroup aspecto;
			
			
			public aspecto_config() {
				
				defecto=new JRadioButton("Metal", config.getAspect().isDefecto());
				manager=new JRadioButton(System.getProperty("os.name"), config.getAspect().isManager());
				nimbus=new JRadioButton("Nimbus", config.getAspect().isNimbus());
				
				defecto.setCursor(new Cursor(Cursor.HAND_CURSOR));
				manager.setCursor(new Cursor(Cursor.HAND_CURSOR));
				nimbus.setCursor(new Cursor(Cursor.HAND_CURSOR));
				
				aspecto=new ButtonGroup();
				
				aspecto.add(defecto);
				aspecto.add(manager);
				aspecto.add(nimbus);
				
				Box vertical=Box.createVerticalBox();
				
				vertical.add(defecto);
				vertical.add(manager);
				vertical.add(nimbus);
				
				add(vertical);
				
				
			}
			
			public boolean isDefecto() {
				return defecto.isSelected();
			}
			
			public boolean isManager() {
				return manager.isSelected();
			}
			
			public boolean isNimbus() {
				return nimbus.isSelected();
			}
			
		}
		
		private class inversor_config extends JPanel{
			
			private static final long serialVersionUID = 1L;
			private JLabel ldinero, lacierto;
			private JSpinner sdinero, sacierto;
			
			public inversor_config() {
				
				default_config_inversor defecto=config.getDefectoInversor();
				
				setLayout(new GridLayout(2, 2));
				
				ldinero=new JLabel("Dinero: ");
				lacierto=new JLabel("Acierto (%): ");
				
				ldinero.setToolTipText("Dinero inicial");
				lacierto.setToolTipText("Probabilidad de acierto");
				
				sdinero=new JSpinner();
				sdinero.setModel(new SpinnerNumberModel(defecto.getDinero_inversor(), 1, 100, 1));
				sdinero.setCursor(new Cursor(Cursor.HAND_CURSOR));
				
				sacierto=new JSpinner();
				sacierto.setModel(new SpinnerNumberModel(defecto.getPorcentaje_acierto(), 0, 100, 5));
				sacierto.setCursor(new Cursor(Cursor.HAND_CURSOR));
				
				add(ldinero);
				add(sdinero);
				add(lacierto);
				add(sacierto);
				
			}
			
			public int getDinero() {
				
				return (int) sdinero.getValue();
				
			}
			
			public int getPorcentajeAcierto() {
				
				return (int) sacierto.getValue();
				
			}
			
		}
		
		private class empresa_config extends JPanel{
						
			private static final long serialVersionUID = 1L;
			private JLabel lacciones, lvalor, lmax, lmin, lvar, laleatorio;
			private JLabel[] ltr;
			private JSpinner sacciones, svalor_acciones, svalor_max_acciones, svalor_min_acciones, svar;
			private JCheckBox caleatorio;
			private JSpinner[] str; 
			
			public int getN_Acciones() {
				return (int) sacciones.getValue();
			}
			
			public int getValor_Acciones() {
				return (int)(double)svalor_acciones.getValue();
			}
			
			public int getValor_Max_Acciones() {
				return (int) svalor_max_acciones.getValue()*1;
			}
			
			public double getValor_Min_Acciones() {
				return (double) svalor_min_acciones.getValue();
			}
			
			public int getPorcentaje_Variacion() {
				return (int) svar.getValue();
			}
			
			public boolean isAleatorio() {
				return caleatorio.isSelected();
			}
			
			public double[] getValor_Trimestres() {
				
				double[] valores=new double[4];
				
				if(!isAleatorio()) {
					for(int i=0; i<4; i++) {
						valores[i]=(double) str[i].getValue();
					}
				}else {
					for(int i=0; i<4; i++) {
						valores[i]=0.0;
					}
				}
				
				return valores;
				
			}
			
			public empresa_config() {
				
				default_config_empresa defecto=config.getDefectoEmpresa();
				
				setLayout(new GridLayout(10, 2));
				
				lacciones=new JLabel("Nº acciones:");
				lvalor=new JLabel("Valor:");
				lmax=new JLabel("Max.:");
				lmin=new JLabel("Min.:");
				lvar=new JLabel("± (%):");
				laleatorio=new JLabel("Aleatorio:");
				
				lacciones.setToolTipText("Número de acciones disponibles");
				lvalor.setToolTipText("Valor inicial de cada acción");
				lmax.setToolTipText("Valor máximo que podrá llegar a tener las acciones");
				lmin.setToolTipText("Valor mínimo que podrán llegar a tener las acciones");
				lvar.setToolTipText("Porcentaje de aumento/decremento que sufrirán las acciones después de ser compradas/vendidas");
				laleatorio.setToolTipText("Previsión aleatoria o manual del aumento/decremento de las acciones al final de cada trimestre");
				
				sacciones=new JSpinner();
				sacciones.setModel(new SpinnerNumberModel(defecto.getN_acciones(), 1, 1000, 10));
								
				svalor_max_acciones=new JSpinner();
				SpinnerNumberModel model_valor_max_acciones=new SpinnerNumberModel(defecto.getValor_max_acciones(), 5, 1000, 1);
				svalor_max_acciones.setModel(model_valor_max_acciones);
				svalor_max_acciones.setCursor(new Cursor(Cursor.HAND_CURSOR));
				
				svalor_min_acciones=new JSpinner(new SpinnerNumberModel(defecto.getValor_min_acciones(), 0.5, 995, 1) {
					
					private static final long serialVersionUID = 1L;

					public Object getNextValue() {
						
						if((double) super.getValue()==0.5) {
							
							double n=1.0;
							
							return n;
							
						}else {
							
							return super.getNextValue();
							
						}
						
					}
					
					public Object getPreviousValue() {
						
						if((double) super.getValue()==1) {
							
							double n=0.5;
							
							return n;
							
						}else {
							
							return super.getPreviousValue();
							
						}
						
					}
					
				});

				svalor_min_acciones.setCursor(new Cursor(Cursor.HAND_CURSOR));
				
				svalor_acciones=new JSpinner();
				SpinnerNumberModel model_valor_acciones=new SpinnerNumberModel((double) defecto.getValor_acciones(), (double) svalor_min_acciones.getValue(), (int) svalor_max_acciones.getValue(), 1);
				svalor_acciones.setModel(model_valor_acciones);
				svalor_acciones.setCursor(new Cursor(Cursor.HAND_CURSOR));

				
				svalor_max_acciones.addChangeListener(new ChangeListener() {
					
					public void stateChanged(ChangeEvent e) {
						
						double max=Double.valueOf((int)svalor_max_acciones.getValue());
						double valor=(double) svalor_acciones.getValue();
						
						if(valor>max) {
							valor=max;
						}
						
						SpinnerNumberModel model_valor_acciones=new SpinnerNumberModel(valor, (double) svalor_min_acciones.getValue(), max, 1);
						svalor_acciones.setModel(model_valor_acciones);
						
					}
				});
				
				svalor_min_acciones.addChangeListener(new ChangeListener() {
					
					public void stateChanged(ChangeEvent e) {
						
						double min=(double) svalor_min_acciones.getValue();
						double valor=(double) svalor_acciones.getValue();
						
						if(valor<min) {
							valor=min;
						}
						
						if(min<1) {
							
							min=1;
							
						}
						
						SpinnerNumberModel model_valor_acciones=new SpinnerNumberModel(valor, min, (int) svalor_max_acciones.getValue(), 1);
						svalor_acciones.setModel(model_valor_acciones);
						
					}
				});
				
				svar=new JSpinner();
				SpinnerNumberModel model_porcentaje_variacion=new SpinnerNumberModel(defecto.getPorcentaje_variacion(), 0, 100, 5);
				svar.setModel(model_porcentaje_variacion);
				svar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
				caleatorio=new JCheckBox();
				caleatorio.setSelected(defecto.isAleatorio());
				
				add(lacciones); add(sacciones);
				add(lvalor); add(svalor_acciones);
				add(lmax); add(svalor_max_acciones);
				add(lmin); add(svalor_min_acciones);
				add(lvar); add(svar);
				add(laleatorio); add(caleatorio);
				
				ltr=new JLabel[4];
				str=new JSpinner[4];
				
				for(int i=0; i<4; i++) {
					
					ltr[i]=new JLabel((i+1) + "º (%)");
					ltr[i].setVisible(!caleatorio.isSelected());
					
					ltr[i].setToolTipText("Previsión del aumento/decremento de las acciones en el " + (i+1) + " trimestre");
					
					str[i]=new JSpinner();
					str[i].setModel(new SpinnerNumberModel(defecto.getValor_trimestres()[i], -1000, 1000, 10));
					str[i].setVisible(!caleatorio.isSelected());
					str[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
					
					add(ltr[i]); add(str[i]);
					
				}
				
				caleatorio.setCursor(new Cursor(Cursor.HAND_CURSOR));
				
				caleatorio.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						
						for(int i=0; i<4; i++) {
							
							ltr[i].setVisible(!caleatorio.isSelected());
							str[i].setVisible(!caleatorio.isSelected());
																					
						}
						
					}
				});
				
			}
			
		
			
		}
		
	}

	private class informe extends JPanel{
		
		private static final long serialVersionUID = 1L;

		public informe(int ejecuciones) {
			
			setLayout(new BorderLayout());
			
			Box informes=Box.createVerticalBox();
			
			for(int i=0; i<ejecuciones; i++) {
				
				JLabel ejecucion=new JLabel((i+1) + "º ejecución");
				
				ejecucion.setToolTipText("Informe de la " + (i+1) + "º ejecución");
				
				informes.add(ejecucion);
				
				ejecucion.addMouseListener(new MouseAdapter() {
					
					public void mousePressed(MouseEvent e) {
						
						JLabel l=(JLabel) e.getSource();
						
						int n=Integer.parseInt(String.valueOf(l.getText().charAt(0)));
						
						JDialog informe_dialog=new JDialog();
						
						URL icon_path=main_TDR_Leo.class.getResource("/TDR/Images/ejecucion.png");	
						Image Icon=Toolkit.getDefaultToolkit().getImage(icon_path);	
						informe_dialog.setIconImage(Icon);
						
						JPanel panel_informe=new JPanel();
						
						Box info=Box.createVerticalBox();
						
						try {
														
							FileReader entrada = new FileReader("Bolsa de valores/logs/log" + (n-1) + ".txt");
							BufferedReader buffer=new BufferedReader(entrada);
							
							String linea="";
							
							while(linea!=null) {	
								
								linea=buffer.readLine();	
								
								if(linea!=null) {
									
									JLabel informacion=new JLabel(linea);

									info.add(informacion);
								}
							}
							
							entrada.close();
							buffer.close();

							
						} catch (IOException e1) {
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null, e1.getMessage(), e1.getClass().toString(), 0);
							e1.printStackTrace();
						}
						
						panel_informe.add(info);
						
						JScrollPane scroll=new JScrollPane(panel_informe);
												
						scroll.getVerticalScrollBar().setUnitIncrement(20);
						scroll.getHorizontalScrollBar().setUnitIncrement(15);
						
						informe_dialog.add(scroll);
						
						int ancho=Toolkit.getDefaultToolkit().getScreenSize().width;
						int alto=Toolkit.getDefaultToolkit().getScreenSize().height;
						
						informe_dialog.setTitle(l.getText());
						
						informe_dialog.setBounds(0, 0, ancho/4, alto/2);
						
						informe_dialog.setModal(false);
						
						informe_dialog.setVisible(true);
						
					}
					
					public void mouseEntered(MouseEvent e) {
						
						JLabel l=(JLabel) e.getSource();
						
						l.setForeground(Color.BLUE);
						
						l.setCursor(new Cursor(Cursor.HAND_CURSOR));
						
					}
					
					public void mouseExited(MouseEvent e) {
						
						JLabel l=(JLabel) e.getSource();
						
						l.setForeground(Color.BLACK);
						
						l.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						
					}
					
				});
				
			}
			
			informes.setBorder(new TitledBorder("Informes"));
			
			JScrollPane scroll=new JScrollPane(informes);
			
			scroll.getVerticalScrollBar().setUnitIncrement(20);
			scroll.getHorizontalScrollBar().setUnitIncrement(15);
			
			add(scroll);
			
		}
		
	}

	private class resumen extends JPanel{
		
		private static final long serialVersionUID = 1L;

		public resumen(archivo_resultado final_log) {
			
			setLayout(new BorderLayout());
			
			Box resumen_vertical=Box.createVerticalBox();
						
			ArrayList<inversor[]> resumen=final_log.getResumen();
			
			for(int i=0; i<resumen.size(); i++) {
				
				Box ejecucion=Box.createVerticalBox();
				
				inversor[] inversores=resumen.get(i);
				
				for(inversor in: inversores) {
					
					JLabel label=new JLabel("Inversor [" + in.getID() + "] - Dinero: " + in.getDinero() + " rupias");
									
					ejecucion.add(label);
					
				}
				
				ejecucion.setBorder(new TitledBorder("Ejecución (" + (i+1) + ")"));
				
				resumen_vertical.add(ejecucion);
				
			}
			
			resumen_vertical.setBorder(new TitledBorder("Resumen"));
			
			JScrollPane scroll=new JScrollPane(resumen_vertical);
			
			scroll.getVerticalScrollBar().setUnitIncrement(20);
			scroll.getHorizontalScrollBar().setUnitIncrement(15);
			
			add(scroll);
			
		}
		
	}

	private class resultados extends JPanel{
		
		private static final long serialVersionUID = 1L;
		private String[] titColumnas;
		private String[] [] tabla;
		
		public resultados(archivo_resultado final_log, String[] nombre_inversores) {
			
			int [] [] posiciones=final_log.getTabla_Posiciones();
			tabla=new String[posiciones.length][posiciones[0].length + 1];
			
			for(int i=0; i<posiciones.length; i++) {
				
				tabla[i][0]=nombre_inversores[i];
				
				for(int j=0; j<posiciones[i].length; j++) {
					
					tabla[i][(j+1)]=String.valueOf(posiciones[i][j]);
					
				}
				
			}
			
			columnas();
			
			setLayout(new BorderLayout());
			
			JTable tabla_resultados=new table(tabla, titColumnas);
			
			tabla_resultados.setAutoCreateRowSorter(true);
			
			JScrollPane scroll=new JScrollPane(tabla_resultados);
			
			scroll.getVerticalScrollBar().setUnitIncrement(20);
			scroll.getHorizontalScrollBar().setUnitIncrement(15);
			
			add(scroll, BorderLayout.CENTER);
			
		}
		
		private void columnas() {
			
			titColumnas=new String[tabla[0].length];
			
			titColumnas[0]="Inversor";
			
			for(int i=1; i<titColumnas.length; i++) {
				
				titColumnas[i]=i + "º";
				
			}
			
		}
		
		private class table extends JTable{
			
			private static final long serialVersionUID = 1L;

			public table(String[][] _tabla, String[] _titColumnas) {
				
				super(_tabla, _titColumnas);
				
			}
			
			public boolean isCellEditable(int row, int column) {
				
				return false;
				
			}
			
		}
		
	}


	private class calculando extends JPanel{
		
		private static final long serialVersionUID = 1L;

		public calculando() {
					
			setLayout(null);
				    
		    updateUI();
			
			
		}
		
		protected void paintComponent(Graphics g) {
			
		    super.paintComponent(g);
		    
	        Graphics2D g2d = (Graphics2D) g;
		    
		    Image img = new ImageIcon(main_TDR_Leo.class.getResource("/TDR/Images/procesando.gif")).getImage();
		    g.drawImage(img, getWidth()/2-(getHeight()/4)/2, getHeight()/2-(getHeight()/4)/2-16, getHeight()/4, getHeight()/4, this);
		    
	        g2d.setColor(Color.RED);
	        g2d.setFont(new Font("Verdana",Font.PLAIN,20));
	        g2d.drawString("Procesando datos. Porfavor, espere", getWidth()/2-(getHeight()/4)/2 + (getHeight()/4)/2 - 150, getHeight()/2-(getHeight()/4)/2 + getHeight()/4 + 16);
	        
		    updateUI();
		}
		
	}
	
}

class inversor_panel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private Box vertical;
	private JScrollPane scroll_inversores;
	private ArrayList<nuevo_inversor> inversores_panel;
	private ArrayList<Integer> lista_numsID; 
	private int num_inversores;
	private JLabel Lnum_inversores;
	private JSpinner Snum_inversores;
	private JCheckBox Cauto;
	private JButton Bok;
	
	public inversor_panel(int ancho_panel, int alto_panel) {
		
		setLayout(null);
		
		JPanel contenido=new JPanel();
		
		vertical=Box.createVerticalBox();
		
		contenido.setLayout(new BorderLayout());
		
		JPanel barra_superior=new JPanel();
		
		barra_superior.setLayout(null);
		
		barra_superior.setBackground(Color.GRAY.brighter());
		
		JLabel Lnombre=new JLabel("Nombre");
		JLabel Ldinero=new JLabel("Dinero");
		JLabel Lacierto=new JLabel("Acierto");
		
		Lnombre.setToolTipText("Nombre único para el inversor");
		Ldinero.setToolTipText("Dinero inicial");
		Lacierto.setToolTipText("Probabilidad de acierto");
		
		Lnombre.setCursor(new Cursor(Cursor.HAND_CURSOR));
		Ldinero.setCursor(new Cursor(Cursor.HAND_CURSOR));
		Lacierto.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		barra_superior.setPreferredSize(new Dimension(ancho_panel-8,30));
				
		contenido.add(barra_superior, BorderLayout.NORTH);
		contenido.add(vertical, BorderLayout.CENTER);

		
		scroll_inversores=new JScrollPane(contenido);
		
		scroll_inversores.getVerticalScrollBar().setUnitIncrement(1);
		
		scroll_inversores.setBounds(0, 30, ancho_panel-2, alto_panel-95);

		vertical.setBackground(Color.WHITE);
		contenido.setBackground(Color.WHITE);
		scroll_inversores.setBackground(Color.WHITE);

		
		add(scroll_inversores);
				
		setBackground(Color.WHITE);
		
		
		inversores_panel=new ArrayList<nuevo_inversor>();
		
		lista_numsID=new ArrayList<Integer>();
		
		num_inversores=0;
		
		
		Lnum_inversores=new JLabel("Nº de inversores: " + num_inversores);
		
		Lnum_inversores.setToolTipText("Número total de inversores");
		
		final int LnumW=150;
		final int LnumH=30;
		final int LnumX=scroll_inversores.getX() + 2;
		final int LnumY=scroll_inversores.getY()-30;
		
		Lnum_inversores.setBounds(LnumX, LnumY, LnumW, LnumH);
		
		add(Lnum_inversores);
		
		
		Snum_inversores=new JSpinner();
		
		SpinnerNumberModel model_num_inversores=new SpinnerNumberModel(1, 1, 1000, 1);
		
		Snum_inversores.setModel(model_num_inversores);
		
		final int SnumW=65;
		final int SnumH=30;
		final int SnumX=LnumX + 100;
		final int SnumY=LnumY;
		
		Snum_inversores.setBounds(SnumX, SnumY, SnumW, SnumH);
		
		add(Snum_inversores);
		
		Snum_inversores.setVisible(false);
		
		Snum_inversores.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		
		Bok=new JButton("");
		
		Bok.setToolTipText("Añadir");
		
		final int BokW=20;
		final int BokH=20;
		final int BokX=SnumX + SnumW + 5;
		final int BokY=SnumY;
		
		Bok.setBounds(BokX, BokY, BokW, BokH);
		
		Bok.setIcon(new ImageIcon(main_TDR_Leo.class.getResource("/TDR/Images/ok.png")));
		Bok.setOpaque(false);
		Bok.setFocusPainted(false);
		Bok.setBorderPainted(false); 
		Bok.setContentAreaFilled(false); 
		
		Bok.addMouseListener(new MouseAdapter() {
			
			public void mouseEntered(MouseEvent e) {
				
				Bok.setIcon(new ImageIcon(main_TDR_Leo.class.getResource("/TDR/Images/ok_select.png")));
				
			}
			
			public void mouseExited(MouseEvent e) {
				
				Bok.setIcon(new ImageIcon(main_TDR_Leo.class.getResource("/TDR/Images/ok.png")));
				
			}
			
		});
		
		
		add(Bok);
		
		Bok.setVisible(false);
		
		Bok.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		Bok.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				mensaje m=new mensaje("Se borrarán todos los inversores actuales", "¿Quiere continuar?", "Warning", 0);
				
				int aceptar=m.show();
				
				if(aceptar==0) {
					
					Iterator<nuevo_inversor> it=inversores_panel.iterator();
										
					while(it.hasNext()) {
						
						nuevo_inversor inversor=it.next();
						
						quitInversor(inversor.getNum(), inversor, it);
						
					}
										
					for(int i=0; i<(int) Snum_inversores.getValue(); i++) {
						
						nuevo_inversor_panel();
						
					}
				}
				
			}
		});
		
		
		JButton nuevo_inversor=new JButton("Añadir inversor");

		nuevo_inversor.setToolTipText("Añadir nuevo inversor");
		
		nuevo_inversor.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		nuevo_inversor.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				if(num_inversores==1000) {
					
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, "No se pueden añadir más de 1000 inversores", "Error", 0);
					
					
				}else {
					
					nuevo_inversor_panel();
					
				}
												
			}
		});
		
		final int buttonW=150;
		final int buttonH=30;
		final int buttonX=scroll_inversores.getX()+((scroll_inversores.getWidth()/2)-(buttonW/2));
		final int buttonY=scroll_inversores.getY()+scroll_inversores.getHeight()+10;
		
		nuevo_inversor.setBounds(buttonX, buttonY, buttonW, buttonH);
		
		add(nuevo_inversor);
		
		
		Cauto=new JCheckBox("Automático");
		
		Cauto.setToolTipText("Añadir un número de inversores automàticamente");
		
		Cauto.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		final int CautoW=100;
		final int CautoH=30;
		final int CautoX=BokX + BokW + 5;
		final int CautoY=LnumY;
		
		Cauto.setBounds(CautoX, CautoY, CautoW, CautoH);
		
		Cauto.setBackground(Color.WHITE);
		
		add(Cauto);
				
		Cauto.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				if(Cauto.isSelected()) {
										
					Lnum_inversores.setText("Nº de inversores:");
					
					Lnum_inversores.setSize(100, LnumH);
					
					nuevo_inversor.setEnabled(false);
					
					Snum_inversores.setValue(num_inversores);
					
					Snum_inversores.setVisible(true);
					
					Bok.setVisible(true);
					
				}else {
										
					Lnum_inversores.setSize(LnumW, LnumH);
					
					//num_inversores=(int) Snum_inversores.getValue();
					
					Lnum_inversores.setText("Nº de inversores: " + num_inversores);
					
					nuevo_inversor.setEnabled(true);
					
					Snum_inversores.setVisible(false);
					
					Bok.setVisible(false);
					
				}
				
			}
		});
		
		nuevo_inversor_panel();
		
		TDR.inversor_panel.nuevo_inversor in=inversores_panel.get(0);
		
		Lnombre.setBounds(in.getCrd_Nombre()[0], 5, in.getCrd_Nombre()[1], 20);
				
		Lnombre.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {
								
				if(Lnombre.isOpaque()) {
					
					Lnombre.setBackground(null);
					Lnombre.setOpaque(false);
					for(TDR.inversor_panel.nuevo_inversor i: inversores_panel) {
						i.select_Nombre(false, null);
					}
					
				}else {
					
					Lnombre.setOpaque(true);
					Lnombre.setBackground(Color.orange.brighter());
					for(TDR.inversor_panel.nuevo_inversor i: inversores_panel) {
						i.select_Nombre(true, Color.orange.brighter());
					}
					
				}
				
			}
			
		});
		
		barra_superior.add(Lnombre);
		
		Ldinero.setBounds(in.getCrd_Dinero()[0], 5, in.getCrd_Dinero()[1], 20);
		
		Ldinero.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {
								
				if(Ldinero.isOpaque()) {
					
					Ldinero.setBackground(null);
					Ldinero.setOpaque(false);
					for(TDR.inversor_panel.nuevo_inversor i: inversores_panel) {
						i.select_Dineor(false, null);
					}
					
				}else {
					
					Ldinero.setOpaque(true);
					Ldinero.setBackground(Color.GREEN.brighter());
					for(TDR.inversor_panel.nuevo_inversor i: inversores_panel) {
						i.select_Dineor(true, Color.GREEN.brighter());
					}
					
				}
				
			}
			
		});
		
		barra_superior.add(Ldinero);
		
		Lacierto.setBounds(in.getCrd_Porcentaje()[0], 5, in.getCrd_Porcentaje()[1], 20);
		
		Lacierto.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {
								
				if(Lacierto.isOpaque()) {
					
					Lacierto.setBackground(null);
					Lacierto.setOpaque(false);
					for(TDR.inversor_panel.nuevo_inversor i: inversores_panel) {
						i.select_Porcentaje(false, null);
					}
					
				}else {
					
					Lacierto.setOpaque(true);
					Lacierto.setBackground(Color.cyan.brighter());
					for(TDR.inversor_panel.nuevo_inversor i: inversores_panel) {
						i.select_Porcentaje(true, Color.cyan.brighter());
					}
					
				}
				
			}
			
		});
		
		barra_superior.add(Lacierto);
		
		if((CautoX + CautoW)>scroll_inversores.getWidth()) {
			
			setPreferredSize(new Dimension(CautoX + CautoW - 5, this.getHeight()));
			
		}else {
			
			setPreferredSize(new Dimension(scroll_inversores.getWidth()-3, this.getHeight()));
			
		}
		
		updateUI();

	}
	
	private void nuevo_inversor_panel() {
		
		num_inversores++;
		
		int num_entregar=num_inversores;
		
		boolean repetido=false;
		
		for(int nums: lista_numsID) {
			
			if(num_entregar==nums) {
				
				repetido=true;
				
			}			
		}
		
		if(repetido) {
			
			num_entregar=1;
			
			while(repetido) {
				
				repetido=false;
				
				for(int nums2: lista_numsID) {
					
					if(num_entregar==nums2) {
						
						repetido=true;
						
					}
					
				}
				
				if(repetido) {
					num_entregar++;
				}
				
			}
			
		}
				
		nuevo_inversor inversor=new nuevo_inversor(num_entregar);
		
		lista_numsID.add(num_entregar);
		
		inversores_panel.add(inversor);
		
		vertical.add(inversor);
						
		if(!Cauto.isSelected()) {
			Lnum_inversores.setText("Nº de inversores: " + num_inversores);
		}
		
		setVelScroll(num_inversores);
				
		updateUI();
		
	}
	
	public void nuevo_inversor_panel(String nombre, int dinero, int acierto) {
		
		num_inversores++;
		
		nuevo_inversor inversor=new nuevo_inversor(nombre, dinero, acierto);
				
		inversores_panel.add(inversor);
		
		vertical.add(inversor);
						
		if(!Cauto.isSelected()) {
			Lnum_inversores.setText("Nº de inversores: " + num_inversores);
		}
		
		setVelScroll(num_inversores);
				
		updateUI();
		
	}
	
	private void quitInversor(int num_inversor, nuevo_inversor inversor) {
		
		if(Cauto.isSelected()) {
			
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, "No se pueden eliminar inversores mientras este el modo automático activado", "Error", 0);
			
		}else {
		
			if(num_inversores==1) {
				
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(null, "Como mínimo debe haber un inversor", "Error", 0);
				
			}else {
			
				num_inversores--;
				
				int posicion=-1;
				
				for(int i=0; i<lista_numsID.size(); i++) {
					
					if(num_inversor==lista_numsID.get(i)) {
						
						posicion=i;
						
						break;
						
					}
					
				}
				
				if(lista_numsID.size()>0) {
					
					lista_numsID.remove(posicion);
					
				}
				
				vertical.remove(inversor);
				
				inversores_panel.remove(inversor);
				
				Lnum_inversores.setText("Nº de inversores: " + num_inversores);
				
				setVelScroll(num_inversores);
				
				updateUI();
				
			}
			
		}
	}
	
	private void quitInversor(int num_inversor, nuevo_inversor inversor, Iterator<nuevo_inversor> it) {
			
		num_inversores--;
				
		int posicion=-1;
				
		for(int i=0; i<lista_numsID.size(); i++) {
					
			if(num_inversor==lista_numsID.get(i)) {
						
				posicion=i;
						
				break;
						
			}
					
		}
				
		if(lista_numsID.size()>0) {
					
			lista_numsID.remove(posicion);
					
		}
				
		vertical.remove(inversor);
				
		it.remove();
		
		setVelScroll(num_inversores);
								
		updateUI();
			
	}
	
	public void quitAllInversor() {
		
		Iterator<nuevo_inversor> it=inversores_panel.iterator();
		
		while(it.hasNext()) {
			
			nuevo_inversor inversor=it.next();
			
			quitInversor(inversor.getNum(), inversor, it);
			
		}
		
	}
	
	private void setVelScroll(int num) {
		
		scroll_inversores.getVerticalScrollBar().setUnitIncrement(num);
		
	}
	
	public int getNum_inversores() {
		
		return num_inversores;
		
	}
	
	public String getNombre(int i) {
		
		return inversores_panel.get(i).getNombre();
		
	}
	
	public int getDinero(int i) {
		
		return inversores_panel.get(i).getDinero();
		
	}
	
	public int getPorcentaje(int i) {
		
		return inversores_panel.get(i).getPorcentaje();
		
	}
	
	private class nuevo_inversor extends JPanel{
		
		private static final long serialVersionUID = 1L;
		private JLabel linversor, signo;
		private JTextField tnombre;
		private JSpinner sporcentaje, sdinero;
		private JButton quit;
		private nuevo_inversor yo;
				
		private int num;
		
		public nuevo_inversor(int _num) {
			
			default_config_inversor defecto=config.getDefectoInversor();
			
			setLayout(null);

			//setBackground(Color.WHITE);
			
			num=_num;

			linversor=new JLabel("Inversor: ");
			tnombre=new JTextField(String.valueOf(num), 10);
			tnombre.setOpaque(false);
			sdinero=new JSpinner();
			SpinnerNumberModel model_dinero=new SpinnerNumberModel(defecto.getDinero_inversor(), 1, 100, 1);
			sdinero.setModel(model_dinero);
			((JSpinner.DefaultEditor) sdinero.getEditor()).getTextField().setOpaque(false);
			sdinero.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			sporcentaje=new JSpinner();
			SpinnerNumberModel model=new SpinnerNumberModel(defecto.getPorcentaje_acierto(), 0, 100, 5);
			sporcentaje.setModel(model);
			sporcentaje.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			((JSpinner.DefaultEditor) sporcentaje.getEditor()).getTextField().setOpaque(false);
			signo=new JLabel("%");
			
			quit=new JButton("");
			quit.setIcon(new ImageIcon(main_TDR_Leo.class.getResource("/TDR/Images/eliminar.png")));
			quit.setOpaque(false);
			quit.setFocusPainted(false);
			quit.setBorderPainted(false); 
			quit.setContentAreaFilled(false); 
			quit.setToolTipText("Eliminar inversor");
			
			quit.addMouseListener(new MouseAdapter() {
				
				public void mouseEntered(MouseEvent e) {
					
					quit.setIcon(new ImageIcon(main_TDR_Leo.class.getResource("/TDR/Images/eliminar_select.png")));
					
				}
				
				public void mouseExited(MouseEvent e) {
					
					quit.setIcon(new ImageIcon(main_TDR_Leo.class.getResource("/TDR/Images/eliminar.png")));
					
				}
				
			});
			quit.setCursor(new Cursor(Cursor.HAND_CURSOR));
					
			yo=this;
						
			final int spaceX=5;
			final int spaceY=5;
			
			final int H=25;
			
			final int inversorX=spaceX-1;
			final int inversorY=spaceY;
			final int inversorW=60;
			final int inversorH=H;
			
			linversor.setBounds(inversorX, inversorY, inversorW, inversorH);
			
			final int nombreX=inversorX + inversorW + spaceX;
			final int nombreY=inversorY;
			final int nombreW=100;
			final int nombreH=H;
			
			tnombre.setBounds(nombreX, nombreY, nombreW, nombreH);
			
			final int dineroX=nombreX + nombreW + spaceX;
			final int dineroY=nombreY;
			final int dineroW=55;
			final int dineroH=H;
			
			sdinero.setBounds(dineroX, dineroY, dineroW, dineroH);
			
			final int porcentajeX=dineroX + dineroW + spaceX;
			final int porcentajeY=dineroY;
			final int porcentajeW=55;
			final int porcentajeH=H;
			
			sporcentaje.setBounds(porcentajeX, porcentajeY, porcentajeW, porcentajeH);
			
			final int signoX=porcentajeX + porcentajeW + 2;
			final int signoY=porcentajeY;
			final int signoW=20;
			final int signoH=H;
			
			signo.setBounds(signoX, signoY, signoW, signoH);
			
			final int quitX=signoX + signoW + spaceX-1;
			final int quitY=signoY;
			final int quitW=50;
			final int quitH=H;
			
			quit.setBounds(quitX, quitY, quitW, quitH);
			
			add(linversor);
			add(tnombre);
			add(sdinero);
			add(sporcentaje);
			add(signo);
			add(quit);
			
			final int W=spaceX + inversorW + spaceX + nombreW + spaceX + dineroW + porcentajeW + 2 + signoW + spaceX + quitW + spaceX*2;
			
			this.setPreferredSize(new Dimension(W-2,H + spaceY +15));
			
			quit.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					quitInversor(num, yo);
					
				}
			});
							
		}
		
		public nuevo_inversor(String nombre, int dinero, int porcentaje) {
			
			this(0);
			
			tnombre.setText(nombre);
			sdinero.setValue(dinero);
			sporcentaje.setValue(porcentaje);
			
			updateUI();
			
		}
		
		public int[] getCrd_Nombre() {
			
			int[] crd={tnombre.getLocation().x, tnombre.getWidth()};
			
			return crd;
			
		}
		
		public void select_Nombre(boolean opac, Color c) {
			
			tnombre.setOpaque(opac);
			tnombre.setBackground(c);
			
		}
		
		public int[] getCrd_Dinero() {
			
			int[] crd={sdinero.getLocation().x, sdinero.getWidth()};
			
			return crd;
			
		}		
		
		public void select_Dineor(boolean opac, Color c) {
			
			((JSpinner.DefaultEditor) sdinero.getEditor()).getTextField().setOpaque(opac);
			((JSpinner.DefaultEditor) sdinero.getEditor()).getTextField().setBackground(c);;
			
		}
		
		public int[] getCrd_Porcentaje() {
			
			int[] crd={sporcentaje.getLocation().x, sporcentaje.getWidth() + 2 + signo.getWidth()};
			
			return crd;
			
		}
		
		public void select_Porcentaje(boolean opac, Color c) {
			
			((JSpinner.DefaultEditor) sporcentaje.getEditor()).getTextField().setOpaque(opac);
			((JSpinner.DefaultEditor) sporcentaje.getEditor()).getTextField().setBackground(c);
			
		}
		
		public String getNombre() {
			
			return tnombre.getText();
			
		}
		
		public int getDinero() {
			
			return (int) sdinero.getValue();
			
		}
		
		public int getPorcentaje() {
			
			return (int) sporcentaje.getValue();
			
		}
		
		public int getNum() {
			
			return num;
			
		}
		
	}
	
}

class empresa_panel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private Box vertical;
	private JScrollPane scroll_empresas;
	private ArrayList<nueva_empresa> empresas_panel;
	private ArrayList<Integer> lista_numsID; 
	private int num_empresas;
	private JLabel Lnum_empresas;
	private JSpinner Snum_empresas;
	private JCheckBox Cauto;
	private JButton Bok;
	
	public empresa_panel(int ancho, int alto) {
		
		setLayout(null);
		
		JPanel contenido=new JPanel();
		
		vertical=Box.createVerticalBox();
		
		contenido.setLayout(new BorderLayout());
		
		JPanel barra_superior=new JPanel();
		
		barra_superior.setLayout(null);
		
		barra_superior.setBackground(Color.GRAY.brighter());
		
		JLabel Lnombre=new JLabel("Nombre");
		JLabel Lacciones=new JLabel("Nº acciones");
		JLabel Lvalor=new JLabel("Valor");
		JLabel Lmax=new JLabel("Max.");
		JLabel Lmin=new JLabel("Min.");
		JLabel Lporcentaje=new JLabel("± %");
		JLabel Laleatorio=new JLabel("Aleatorio");
		JLabel[] Ltrimestres=new JLabel[4];
		
		Lnombre.setToolTipText("Nombre única para la empresa");
		Lacciones.setToolTipText("Número de acciones disponibles");
		Lvalor.setToolTipText("Valor inicial de cada acción");
		Lmax.setToolTipText("Valor máximo que podrá llegar a tener las acciones");
		Lmin.setToolTipText("Valor mínimo que podrán llegar a tener las acciones");
		Lporcentaje.setToolTipText("Porcentaje de aumento/decremento que sufrirán las acciones después de ser compradas/vendidas");
		Laleatorio.setToolTipText("Previsión aleatoria o manual del aumento/decremento de las acciones al final de cada trimestre");
		
		for(int i=0; i< Ltrimestres.length; i++) {
			
			Ltrimestres[i]=new JLabel((i+1) + "º trimestre");
			
			Ltrimestres[i].setToolTipText("Previsión del aumento/decremento de las acciones en el " + (i+1) + " trimestre");
			
		}
				
		barra_superior.setPreferredSize(new Dimension(ancho-5,30));
				
		contenido.add(barra_superior, BorderLayout.NORTH);
		contenido.add(vertical, BorderLayout.CENTER);

		
		scroll_empresas=new JScrollPane(contenido);
		
		scroll_empresas.getVerticalScrollBar().setUnitIncrement(1);
		
		scroll_empresas.getHorizontalScrollBar().setUnitIncrement(10);
		
		scroll_empresas.setBounds(0, 30, ancho-2, alto-95);
				
		add(scroll_empresas);
		
		setBackground(Color.WHITE);
		
		empresas_panel=new ArrayList<nueva_empresa>();
		
		lista_numsID=new ArrayList<Integer>();
		
		num_empresas=0;
		
		
		Lnum_empresas=new JLabel("Nº de empresas: " + num_empresas);
		
		Lnum_empresas.setToolTipText("Número total de empresas");
		
		final int LnumW=150;
		final int LnumH=30;
		final int LnumX=scroll_empresas.getX() + 2;
		final int LnumY=scroll_empresas.getY()-30;
		
		Lnum_empresas.setBounds(LnumX, LnumY, LnumW, LnumH);
		
		add(Lnum_empresas);
		
		
		Snum_empresas=new JSpinner();
		
		SpinnerNumberModel model_num_empresas=new SpinnerNumberModel(1, 1, 1000, 1);
		
		Snum_empresas.setModel(model_num_empresas);
		
		Snum_empresas.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		final int SnumW=65;
		final int SnumH=30;
		final int SnumX=LnumX + 100;
		final int SnumY=LnumY;
		
		Snum_empresas.setBounds(SnumX, SnumY, SnumW, SnumH);
		
		add(Snum_empresas);
		
		Snum_empresas.setVisible(false);
		
		
		Bok=new JButton("");
		
		Bok.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		Bok=new JButton("");
		
		Bok.setToolTipText("Añadir");
		
		final int BokW=20;
		final int BokH=20;
		final int BokX=SnumX + SnumW + 5;
		final int BokY=SnumY;
		
		Bok.setBounds(BokX, BokY, BokW, BokH);
		
		Bok.setIcon(new ImageIcon(main_TDR_Leo.class.getResource("/TDR/Images/ok.png")));
		Bok.setOpaque(false);
		Bok.setFocusPainted(false);
		Bok.setBorderPainted(false); 
		Bok.setContentAreaFilled(false); 
		
		Bok.addMouseListener(new MouseAdapter() {
			
			public void mouseEntered(MouseEvent e) {
				
				Bok.setIcon(new ImageIcon(main_TDR_Leo.class.getResource("/TDR/Images/ok_select.png")));
				
			}
			
			public void mouseExited(MouseEvent e) {
				
				Bok.setIcon(new ImageIcon(main_TDR_Leo.class.getResource("/TDR/Images/ok.png")));
				
			}
			
		});

		Bok.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		Bok.setBounds(BokX, BokY, BokW, BokH);
		
		add(Bok);
		
		Bok.setVisible(false);
		
		Bok.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				mensaje m=new mensaje("Se borrarán todas las empresas actuales", "¿Quiere continuar?", "Warning", 1);
				
				int aceptar=m.show();				
				if(aceptar==0) {
					
					Iterator<nueva_empresa> it=empresas_panel.iterator();
										
					while(it.hasNext()) {
						
						nueva_empresa empresa=it.next();
						
						quitEmpresa(empresa.getNum(), empresa, it);
						
					}
										
					for(int i=0; i<(int) Snum_empresas.getValue(); i++) {
						
						nueva_empresa_panel();
						
					}
				}
				
			}
		});
		
		
		JButton nueva_empresa=new JButton("Añadir empresa");

		nueva_empresa.setToolTipText("Añadir nueva empresa");
		
		nueva_empresa.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		nueva_empresa.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				if(num_empresas==1000) {
					
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, "No se pueden añadir más de 1000 empresas", "Error", 0);
					
				}else {
					
					nueva_empresa_panel();
					
				}
												
			}
		});
		
		final int buttonW=150;
		final int buttonH=30;
		final int buttonX=scroll_empresas.getX()+((scroll_empresas.getWidth()/2)-(buttonW/2));
		final int buttonY=scroll_empresas.getY()+scroll_empresas.getHeight()+10;
		
		nueva_empresa.setBounds(buttonX, buttonY, buttonW, buttonH);
		
		add(nueva_empresa);
		
		
		Cauto=new JCheckBox("Automático");
		
		Cauto.setToolTipText("Añadir un número de empresas automaticamente");
		
		Cauto.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		final int CautoW=100;
		final int CautoH=30;
		final int CautoX=BokX + BokW + 5;
		final int CautoY=LnumY;
		
		Cauto.setBounds(CautoX, CautoY, CautoW, CautoH);
		
		Cauto.setBackground(Color.WHITE);
		
		add(Cauto);
				
		Cauto.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				if(Cauto.isSelected()) {
										
					Lnum_empresas.setText("Nº de empresas:");
					
					Lnum_empresas.setSize(100, LnumH);
					
					nueva_empresa.setEnabled(false);
					
					Snum_empresas.setValue(num_empresas);
					
					Snum_empresas.setVisible(true);
					
					Bok.setVisible(true);
					
				}else {
										
					Lnum_empresas.setSize(LnumW, LnumH);
					
					//num_inversores=(int) Snum_inversores.getValue();
					
					Lnum_empresas.setText("Nº de empresas: " + num_empresas);
					
					nueva_empresa.setEnabled(true);
					
					Snum_empresas.setVisible(false);
					
					Bok.setVisible(false);
					
				}
				
			}
		});
								
		nueva_empresa_panel();
		
		nueva_empresa em=empresas_panel.get(0);
		
		final int spaceY=5;
		final int H=20;
		
		Lnombre.setBounds(em.getNombre_crd()[0], spaceY, em.getNombre_crd()[1], H);
		
		Lnombre.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		Lnombre.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {
				
				if(Lnombre.isOpaque()) {
					
					Lnombre.setBackground(null);
					Lnombre.setOpaque(false);
					
					for(nueva_empresa empr: empresas_panel) {
						
						empr.select_Nombre(false, null);
						
					}
					
				}else {
					
					Color c=Color.orange.brighter();
					
					Lnombre.setOpaque(true);
					Lnombre.setBackground(c);
					
					for(nueva_empresa empr: empresas_panel) {
						
						empr.select_Nombre(true, c);
						
					}
					
				}
				
			}
			
		});
		
		barra_superior.add(Lnombre);
		
		Lacciones.setBounds(em.getAcciones_crd()[0], spaceY, em.getAcciones_crd()[1], H);
		
		Lacciones.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		Lacciones.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {
				
				if(Lacciones.isOpaque()) {
					
					Lacciones.setBackground(null);
					Lacciones.setOpaque(false);
					
					for(nueva_empresa empr: empresas_panel) {
						
						empr.select_Acciones(false, null);
						
					}
					
				}else {
					
					Color c=Color.GREEN;
					
					Lacciones.setOpaque(true);
					Lacciones.setBackground(c);
					
					for(nueva_empresa empr: empresas_panel) {
						
						empr.select_Acciones(true, c);
						
					}
					
				}
				
			}
			
		});

		
		barra_superior.add(Lacciones);
		
		Lvalor.setBounds(em.getValorAcciones_crd()[0], spaceY, em.getValorAcciones_crd()[1], H);
		
		Lvalor.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		Lvalor.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {
				
				if(Lvalor.isOpaque()) {
					
					Lvalor.setBackground(null);
					Lvalor.setOpaque(false);
					
					for(nueva_empresa empr: empresas_panel) {
						
						empr.select_valorAcciones(false, null);
						
					}
					
				}else {
					
					Color c=Color.cyan;
					
					Lvalor.setOpaque(true);
					Lvalor.setBackground(c);
					
					for(nueva_empresa empr: empresas_panel) {
						
						empr.select_valorAcciones(true, c);
						
					}
					
				}
				
			}
			
		});

		
		barra_superior.add(Lvalor);
		
		Lmax.setBounds(em.getValorMax_crd()[0], spaceY, em.getValorMax_crd()[1], H);
		
		Lmax.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		Lmax.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {
				
				if(Lmax.isOpaque()) {
					
					Lmax.setBackground(null);
					Lmax.setOpaque(false);
					
					for(nueva_empresa empr: empresas_panel) {
						
						empr.select_ValorMax(false, null);
						
					}
					
				}else {
					
					Color c=Color.MAGENTA;
					
					Lmax.setOpaque(true);
					Lmax.setBackground(c);
					
					for(nueva_empresa empr: empresas_panel) {
						
						empr.select_ValorMax(true, c);
						
					}
					
				}
				
			}
			
		});

		
		barra_superior.add(Lmax);
		
		Lmin.setBounds(em.getValorMin_crd()[0], spaceY, em.getValorMin_crd()[1], H);
		
		Lmin.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		Lmin.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {
				
				if(Lmin.isOpaque()) {
					
					Lmin.setBackground(null);
					Lmin.setOpaque(false);
					
					for(nueva_empresa empr: empresas_panel) {
						
						empr.select_ValorMin(false, null);
						
					}
					
				}else {
					
					Color c=Color.ORANGE;
					
					Lmin.setOpaque(true);
					Lmin.setBackground(c);
					
					for(nueva_empresa empr: empresas_panel) {
						
						empr.select_ValorMin(true, c);
						
					}
					
				}
				
			}
			
		});

		
		barra_superior.add(Lmin);
		
		Lporcentaje.setBounds(em.getPorcentaje_crd()[0], spaceY, em.getPorcentaje_crd()[1], H);
		
		Lporcentaje.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		Lporcentaje.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {
				
				if(Lporcentaje.isOpaque()) {
					
					Lporcentaje.setBackground(null);
					Lporcentaje.setOpaque(false);
					
					for(nueva_empresa empr: empresas_panel) {
						
						empr.select_Porcentaje(false, null);
						
					}
					
				}else {
					
					Color c=Color.CYAN.darker().darker();
					
					Lporcentaje.setOpaque(true);
					Lporcentaje.setBackground(c);
					
					for(nueva_empresa empr: empresas_panel) {
						
						empr.select_Porcentaje(true, c);
						
					}
					
				}
				
			}
			
		});

		
		barra_superior.add(Lporcentaje);
		
		Laleatorio.setBounds(em.getAleatorio_crd()[0], spaceY, em.getAleatorio_crd()[1], H);
		
		//Laleatorio.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		barra_superior.add(Laleatorio);
		
		for(int i=0; i<Ltrimestres.length; i++) {
			
			final int j=i;
			
			Ltrimestres[i].setBounds(em.getTrimestres_crd()[i][0], spaceY, em.getTrimestres_crd()[i][1], H);
			
			Ltrimestres[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			Ltrimestres[i].addMouseListener(new MouseAdapter() {
				
				public void mousePressed(MouseEvent e) {
					
					if(Ltrimestres[j].isOpaque()) {
						
						Ltrimestres[j].setBackground(null);
						Ltrimestres[j].setOpaque(false);
						
						for(nueva_empresa empr: empresas_panel) {
							
							empr.select_Trimestre(false, null, j);
							
						}
						
					}else {
						
						Color[] c= {Color.PINK, Color.RED.brighter(), Color.YELLOW.darker(), Color.GREEN.darker()};
						
						Ltrimestres[j].setOpaque(true);
						Ltrimestres[j].setBackground(c[j]);
						
						for(nueva_empresa empr: empresas_panel) {
							
							empr.select_Trimestre(true, c[j], j);
							
						}
						
					}
					
				}
				
			});

			
			barra_superior.add(Ltrimestres[i]);
			
		}
		
		if((CautoX + CautoW)>scroll_empresas.getWidth()) {
			
			setPreferredSize(new Dimension(CautoX + CautoW - 5, this.getHeight()));
			
		}else {
			
			setPreferredSize(new Dimension(scroll_empresas.getWidth()-3, this.getHeight()));
			
		}		
		
		updateUI();


	}
	
	private void nueva_empresa_panel() {
		
		num_empresas++;
		
		int num_entregar=num_empresas;
		
		boolean repetido=false;
		
		for(int nums: lista_numsID) {
			
			if(num_entregar==nums) {
				
				repetido=true;
				
			}			
		}
		
		if(repetido) {
			
			num_entregar=1;
			
			while(repetido) {
				
				repetido=false;
				
				for(int nums2: lista_numsID) {
					
					if(num_entregar==nums2) {
						
						repetido=true;
						
					}
					
				}
				
				if(repetido) {
					num_entregar++;
				}
				
			}
			
		}
				
		nueva_empresa empresa=new nueva_empresa(num_entregar);
		
		lista_numsID.add(num_entregar);
		
		empresas_panel.add(empresa);
		
		vertical.add(empresa);
						
		if(!Cauto.isSelected()) {
			Lnum_empresas.setText("Nº de empresas: " + num_empresas);
		}
				
		setVelScroll(num_empresas);
		
		updateUI();
		
	}
	
	public void nueva_empresa_panel(String nombre, int acciones, int valor_acciones, int valor_max, double valor_min, int porcentaje, boolean aleatorio, double[] valores) {
		
		num_empresas++;
		
		nueva_empresa empresa=new nueva_empresa(nombre, acciones, valor_acciones, valor_max, valor_min, porcentaje, aleatorio, valores);
				
		empresas_panel.add(empresa);
		
		vertical.add(empresa);
						
		if(!Cauto.isSelected()) {
			Lnum_empresas.setText("Nº de empresas: " + num_empresas);
		}
				
		setVelScroll(num_empresas);
		
		updateUI();
		
	}
	
	private void quitEmpresa(int num_empresa, nueva_empresa empresa) {
		
		if(Cauto.isSelected()) {
			
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, "No se pueden eliminar empresas mientras este el modo automático activado", "Error", 0);
			
		}else {
		
			if(num_empresas==1) {
				
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(null, "Como mínimo debe haber una empresa", "Error", 0);
				
			}else {
			
				num_empresas--;
				
				int posicion=-1;
				
				for(int i=0; i<lista_numsID.size(); i++) {
					
					if(num_empresa==lista_numsID.get(i)) {
						
						posicion=i;
						
						break;
						
					}
					
				}
				
				if(lista_numsID.size()>0) {
					
					lista_numsID.remove(posicion);
					
				}
				
				vertical.remove(empresa);
				
				empresas_panel.remove(empresa);
				
				Lnum_empresas.setText("Nº de empresas: " + num_empresas);
				
				setVelScroll(num_empresas);
				
				updateUI();
				
			}
			
		}
	}
	
	private void quitEmpresa(int num_empresa, nueva_empresa empresa, Iterator<nueva_empresa> it) {
			
		num_empresas--;
				
		int posicion=-1;
				
		for(int i=0; i<lista_numsID.size(); i++) {
					
			if(num_empresa==lista_numsID.get(i)) {
						
				posicion=i;
						
				break;
						
			}
					
		}
				
		if(lista_numsID.size()>0) {
					
			lista_numsID.remove(posicion);
					
		}
				
		vertical.remove(empresa);
				
		it.remove();
					
		setVelScroll(num_empresas);
		
		updateUI();
			
	}
	
	public void quitAllEmpresa() {
		
		Iterator<nueva_empresa> it=empresas_panel.iterator();
		
		while(it.hasNext()) {
			
			nueva_empresa empresa=it.next();
			
			quitEmpresa(empresa.getNum(), empresa, it);
			
		}
		
	}
	
	private void setVelScroll(int num) {
		
		scroll_empresas.getVerticalScrollBar().setUnitIncrement(num);
		
	}
	
	public int getNum_empresas() {
		
		return num_empresas;
		
	}
	
	public String getNombre(int i) {
		
		return empresas_panel.get(i).getNombre();
		
	}
	
	public int getNum_acciones(int i) {
		
		return empresas_panel.get(i).getNum_acciones();
		
	}
	
	public double getValor_acciones(int i) {
		
		return empresas_panel.get(i).getValor_acciones();
		
	}
	
	public int getValor_max_acciones(int i) {
		
		return empresas_panel.get(i).getValor_max_acciones();
		
	}
	
	public double getValor_min_acciones(int i) {
		
		return empresas_panel.get(i).getValor_min_acciones();
		
	}
	
	public int getPorcentaje_variacion(int i) {
		
		return empresas_panel.get(i).getPorcentaje();
		
	}
	
	public boolean getPrevision_aleatorio(int i) {
		
		return empresas_panel.get(i).getAleatorio();
		
	}
	
	public double[] getPrevision_acciones(int i) {
		
		return empresas_panel.get(i).getPrevision();
		
	}
	
	private class nueva_empresa extends JPanel{
		
		private static final long serialVersionUID = 1L;
		private JLabel lempresa;
		private JTextField tnombre;
		private JSpinner sacciones, svalor_acciones, svalor_max_acciones, svalor_min_acciones, sporncentaje_variacion;
		private JButton quit;
		private JCheckBox Caleatorio;
		private JSpinner[] valor_trimestres;
		private nueva_empresa yo;
				
		private final int spaceX;
		private final int nombreX, accionesX, valor_accionesX, valor_maxX, valor_minX, aleatorioX, porcentajeX;
		private final int nombreW, accionesW, valor_accionesW, valor_maxW, valor_minW, aleatorioW, porcentajeW;
				
		private JLabel[] trimestres, signos;
		
		private SpinnerNumberModel model_valor_acciones;
		
		private int num;
		
		public nueva_empresa(int _num) {
						
			default_config_empresa defecto=config.getDefectoEmpresa();
			
			setLayout(null);
			
			num=_num;
			
			lempresa=new JLabel("Empresa:");
			
			spaceX=10;
			final int spaceY=5;
			
			final int H=25;
			
			final int empresaX=spaceX-1;
			final int empresaY=spaceY;
			final int empresaW=60;
			final int empresaH=H;
			
			lempresa.setBounds(empresaX, empresaY, empresaW, empresaH);
			add(lempresa);
			
			tnombre=new JTextField(String.valueOf(num), 10);
			tnombre.setOpaque(false);
			
			nombreX=empresaX + empresaW + spaceX;
			final int nombreY=empresaY;
			nombreW=100;
			final int nombreH=H;
			
			tnombre.setBounds(nombreX, nombreY, nombreW, nombreH);
			add(tnombre);
			
			sacciones=new JSpinner();
			SpinnerNumberModel model_acciones=new SpinnerNumberModel(defecto.getN_acciones(), 1, 1000, 10);
			sacciones.setModel(model_acciones);
			((JSpinner.DefaultEditor) sacciones.getEditor()).getTextField().setOpaque(false);;
			sacciones.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			accionesX=nombreX + nombreW + spaceX;
			final int accionesY=nombreY;
			accionesW=65;
			final int accionesH=H;
			
			sacciones.setBounds(accionesX, accionesY, accionesW, accionesH);
			add(sacciones);
			
			svalor_max_acciones=new JSpinner();
			SpinnerNumberModel model_valor_max_acciones=new SpinnerNumberModel(defecto.getValor_max_acciones(), 5, 1000, 1);
			svalor_max_acciones.setModel(model_valor_max_acciones);
			((JSpinner.DefaultEditor) svalor_max_acciones.getEditor()).getTextField().setOpaque(false);;
			svalor_max_acciones.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			svalor_min_acciones=new JSpinner(new SpinnerNumberModel(defecto.getValor_min_acciones(), 0.5, 995, 1) {

				private static final long serialVersionUID = 1L;

				public Object getNextValue() {
					
					if((double) super.getValue()==0.5) {
						
						double n=1.0;
						
						return n;
						
					}else {
						
						return super.getNextValue();
						
					}
					
				}
				
				public Object getPreviousValue() {
					
					if((double) super.getValue()==1) {
						
						double n=0.5;
						
						return n;
						
					}else {
						
						return super.getPreviousValue();
						
					}
					
				}
				
			});

			((JSpinner.DefaultEditor) svalor_min_acciones.getEditor()).getTextField().setOpaque(false);;
			svalor_min_acciones.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			svalor_acciones=new JSpinner();
			model_valor_acciones=new SpinnerNumberModel((double) defecto.getValor_acciones(), (double) svalor_min_acciones.getValue(), (int) svalor_max_acciones.getValue(), 1);
			svalor_acciones.setModel(model_valor_acciones);
			((JSpinner.DefaultEditor) svalor_acciones.getEditor()).getTextField().setOpaque(false);;
			svalor_acciones.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			valor_accionesX=accionesX + accionesW + spaceX;
			final int valor_accionesY=accionesY;
			valor_accionesW=65;
			final int valor_accionesH=H;
			
			svalor_acciones.setBounds(valor_accionesX, valor_accionesY, valor_accionesW, valor_accionesH);
			
			valor_maxX=valor_accionesX + valor_accionesW + spaceX;
			final int valor_maxY=valor_accionesY;
			valor_maxW=65;
			final int valor_maxH=H;
			
			svalor_max_acciones.setBounds(valor_maxX, valor_maxY, valor_maxW, valor_maxH);
			
			valor_minX=valor_maxX + valor_maxW + spaceX;
			final int valor_minY=valor_maxY;
			valor_minW=50;
			final int valor_minH=H;
			
			svalor_min_acciones.setBounds(valor_minX, valor_minY, valor_minW, valor_minH);
			
			add(svalor_acciones);
			add(svalor_max_acciones);
			add(svalor_min_acciones);
			
			svalor_max_acciones.addChangeListener(new ChangeListener() {
				
				public void stateChanged(ChangeEvent e) {
					
					double max=Double.valueOf((int)svalor_max_acciones.getValue());
					double valor=Double.valueOf(String.valueOf(svalor_acciones.getValue()));
					
					if(valor>max) {
						valor=max;
					}
					
					model_valor_acciones=new SpinnerNumberModel(valor, (double) svalor_min_acciones.getValue(), max, 1);
					svalor_acciones.setModel(model_valor_acciones);
					((JSpinner.DefaultEditor) svalor_acciones.getEditor()).getTextField().setOpaque(false);
					
				}
			});
			
			svalor_min_acciones.addChangeListener(new ChangeListener() {
				
				public void stateChanged(ChangeEvent e) {
					
					double min=(double) svalor_min_acciones.getValue();
					double valor=(double) svalor_acciones.getValue();
					
					if(valor<min) {
						valor=min;
					}
					
					if(min<1) {
						
						min=1;
						
					}
					
					model_valor_acciones=new SpinnerNumberModel(valor, min, (int) svalor_max_acciones.getValue(), 1);
					svalor_acciones.setModel(model_valor_acciones);
					((JSpinner.DefaultEditor) svalor_acciones.getEditor()).getTextField().setOpaque(false);
					
				}
			});
			
			sporncentaje_variacion=new JSpinner();
			SpinnerNumberModel model_porcentaje_variacion=new SpinnerNumberModel(defecto.getPorcentaje_variacion(), 0, 100, 5);
			sporncentaje_variacion.setModel(model_porcentaje_variacion);
			((JSpinner.DefaultEditor) sporncentaje_variacion.getEditor()).getTextField().setOpaque(false);;
			sporncentaje_variacion.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			porcentajeX=valor_minX + valor_minW + spaceX;
			final int porcentajeY=valor_minY;
			porcentajeW=55;
			final int porcentajeH=H;
			
			sporncentaje_variacion.setBounds(porcentajeX, porcentajeY, porcentajeW, porcentajeH);
			add(sporncentaje_variacion);
			
			JLabel signo=new JLabel("%");
			
			final int signoX=porcentajeX + porcentajeW + 2;
			final int signoY=porcentajeY;
			final int signoW=20;
			final int signoH=H;
			
			signo.setBounds(signoX, signoY, signoW, signoH);
			add(signo);			
			
			aleatorioX=signoX + signoW + spaceX;;
			final int aleatorioY=signoY;
			aleatorioW=45;
			final int aleatorioH=H;
			
			int trimestresX=aleatorioX + aleatorioW + spaceX;
			int trimestresY=aleatorioY;
			int trimestresW=70;
			int trimestresH=H;
			
			int valorX=aleatorioX + aleatorioW + spaceX;
			int valorY=aleatorioY;
			int valorW=87 - 22;
			int valorH=H;
			
			int signosX=valorX + valorW + 2;
			int signosY=valorY;
			int signosW=15;
			int signosH=H;
						
			trimestres=new JLabel[4];
			signos=new JLabel[4];
			valor_trimestres=new JSpinner[4];
			
			for(int i=0; i<trimestres.length; i++) {
				
				trimestres[i]=new JLabel("ALEATORIO");
				trimestres[i].setEnabled(false);
				
				valor_trimestres[i]=new JSpinner();
				SpinnerNumberModel model_valor_trimestre=new SpinnerNumberModel(defecto.getValor_trimestres()[i], -1000, 1000, 10);
				valor_trimestres[i].setModel(model_valor_trimestre);
				((JSpinner.DefaultEditor) valor_trimestres[i].getEditor()).getTextField().setOpaque(false);;
				valor_trimestres[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
				
				signos[i]=new JLabel("%");
				
				if(i>0) {
					
					trimestresX=trimestresX + trimestresW + spaceX;
					valorX=signosX + signosW;
					signosX=valorX + valorW + 2;
					
					trimestres[i].setBounds(trimestresX, trimestresY, trimestresW, trimestresH);
					valor_trimestres[i].setBounds(valorX, valorY, valorW, valorH);
					signos[i].setBounds(signosX, signosY, signosW, signosH);
					
				}else {
					
					trimestres[i].setBounds(trimestresX, trimestresY, trimestresW, trimestresH);
					valor_trimestres[i].setBounds(valorX, valorY, valorW, valorH);
					signos[i].setBounds(signosX, signosY, signosW, signosH);
					
				}
				
			}
			
			quit=new JButton("");
			quit.setIcon(new ImageIcon(main_TDR_Leo.class.getResource("/TDR/Images/eliminar.png")));
			quit.setOpaque(false);
			quit.setFocusPainted(false);
			quit.setBorderPainted(false); 
			quit.setContentAreaFilled(false);
			quit.setToolTipText("Eliminar empresa");
			
			quit.addMouseListener(new MouseAdapter() {
				
				public void mouseEntered(MouseEvent e) {
					
					quit.setIcon(new ImageIcon(main_TDR_Leo.class.getResource("/TDR/Images/eliminar_select.png")));
					
				}
				
				public void mouseExited(MouseEvent e) {
					
					quit.setIcon(new ImageIcon(main_TDR_Leo.class.getResource("/TDR/Images/eliminar.png")));
					
				}
				
			});
			
			quit.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			final int quitX=signosX + signosW + spaceX-1;
			final int quitY=trimestresY;
			final int quitW=50;
			final int quitH=H;
			
			quit.setBounds(quitX, quitY, quitW, quitH);
			
			Caleatorio=new JCheckBox("", defecto.isAleatorio());
			
			Caleatorio.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			Caleatorio.setBounds(aleatorioX, aleatorioY, aleatorioW, aleatorioH);
			
			Caleatorio.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {

					yo.remove(quit);
					
					if(Caleatorio.isSelected()) {
												
						for(int i=0; i<valor_trimestres.length; i++) {
							
							yo.remove(valor_trimestres[i]);
							yo.remove(signos[i]);
							
						}
						
						for(int i=0; i<trimestres.length; i++) {
							
							yo.add(trimestres[i]);
							
						}
						
					}else {
						
						for(int i=0; i<trimestres.length; i++) {
							
							yo.remove(trimestres[i]);
							
						}
						
						for(int i=0; i<valor_trimestres.length; i++) {
							
							yo.add(valor_trimestres[i]);
							yo.add(signos[i]);
							
						}
						
					}
					
					add(quit);
					
					yo.updateUI();
					
				}
			});
			
			add(Caleatorio);
			
			for(int i=0; i<trimestres.length; i++) {
				
				if(defecto.isAleatorio()) {
					add(trimestres[i]);
				}else {
					add(valor_trimestres[i]);
				}
								
			}
								
			yo=this;
			
			add(quit);
			
			quit.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					quitEmpresa(num, yo);
					
				}
			});
				
			final int W=spaceX + empresaW + spaceX + nombreW + spaceX + accionesW + spaceX + valor_accionesW + spaceX + valor_maxW + spaceX + valor_minW + spaceX + porcentajeW + 2 + signoW+ spaceX + aleatorioW + spaceX + valorW*4 + signosW*4 + spaceX + quitW + spaceX;
			
			this.setPreferredSize(new Dimension(W-2, H+15));
			
			this.updateUI();
			
		}
		
		public nueva_empresa(String nombre, int acciones, int valor_acciones, int valor_max, double valor_min, int porcentaje, boolean aleatorio, double[] valores) {
			
			this(0);
			
			tnombre.setText(nombre);
			sacciones.setValue(acciones);
			svalor_acciones.setValue(valor_acciones);
			svalor_max_acciones.setValue(valor_max);
			svalor_min_acciones.setValue(valor_min);
			sporncentaje_variacion.setValue(porcentaje);
			Caleatorio.setSelected(aleatorio);
			
			if(!aleatorio) {
				
				for(int i=0; i<valores.length; i++) {
					
					valor_trimestres[i].setValue(valores[i]);
					
				}
				
				for(int i=0; i<trimestres.length; i++) {
					
					remove(trimestres[i]);
					
				}
				
				remove(quit);
				
				for(int i=0; i<valor_trimestres.length; i++) {
					
					add(valor_trimestres[i]);
					add(signos[i]);
					
				}
				
				add(quit);
				
			}
			
			this.updateUI();
			
		}
		
		public String getNombre() {
			
			return tnombre.getText();
			
		}
		
		public int getNum_acciones() {
			
			return (int) sacciones.getValue();
			
		}
		
		public int getValor_max_acciones() {
			
			return (int) svalor_max_acciones.getValue();
			
		}
		
		public double getValor_min_acciones() {
			
			return (double) svalor_min_acciones.getValue();
			
		}
		
		public double getValor_acciones() {
			
			return Double.valueOf(String.valueOf(svalor_acciones.getValue()));
			
		}
		
		public int getPorcentaje() {
			
			return (int) sporncentaje_variacion.getValue();
			
		}
		
		public boolean getAleatorio() {
			
			return Caleatorio.isSelected();
			
		}
		
		public double[] getPrevision() {
			
			if(Caleatorio.isSelected()) {
				
				return null;
				
			}else {
				
				double[] prevision=new double[4];
				
				for(int i=0; i<valor_trimestres.length; i++) {
					
					prevision[i]=(double) valor_trimestres[i].getValue();
					
				}
				
				return prevision;
				
			}
			
		}
		
		public int getNum() {
			
			return num;
			
		}
				
		public int [] getNombre_crd() {
			
			int[] crd= {nombreX, nombreW};
			
			return crd;
			
		}
		
		public void select_Nombre(boolean opac, Color c) {
			
			tnombre.setOpaque(opac);
			tnombre.setBackground(c);
			
		}
		
		
		public int [] getAcciones_crd() {
			
			int [] crd= {accionesX, accionesW};
			
			return crd;
			
		}
		
		public void select_Acciones(boolean opac, Color c) {
			
			((JSpinner.DefaultEditor) sacciones.getEditor()).getTextField().setOpaque(opac);
			((JSpinner.DefaultEditor) sacciones.getEditor()).getTextField().setBackground(c);
			
		}
		
		
		public int [] getValorAcciones_crd() {
			
			int [] crd= {valor_accionesX, valor_accionesW};
			
			return crd;
			
		}	
		
		public void select_valorAcciones(boolean opac, Color c) {
			
			((JSpinner.DefaultEditor) svalor_acciones.getEditor()).getTextField().setOpaque(opac);
			((JSpinner.DefaultEditor) svalor_acciones.getEditor()).getTextField().setBackground(c);
			
		}
		
		
		public int [] getValorMax_crd() {
			
			int [] crd= {valor_maxX, valor_maxW};
			
			return crd;
			
		}	
		
		public void select_ValorMax(boolean opac, Color c) {
			
			((JSpinner.DefaultEditor) svalor_max_acciones.getEditor()).getTextField().setOpaque(opac);
			((JSpinner.DefaultEditor) svalor_max_acciones.getEditor()).getTextField().setBackground(c);
			
		}
		
		public int [] getValorMin_crd() {
			
			int [] crd= {valor_minX, valor_minW};
			
			return crd;
			
		}	
		
		public void select_ValorMin(boolean opac, Color c) {
			
			((JSpinner.DefaultEditor) svalor_min_acciones.getEditor()).getTextField().setOpaque(opac);
			((JSpinner.DefaultEditor) svalor_min_acciones.getEditor()).getTextField().setBackground(c);
			
		}
		
		
		public int [] getPorcentaje_crd() {
			
			int [] crd= {porcentajeX, porcentajeW};
			
			return crd;
			
		}
		
		public void select_Porcentaje(boolean opac, Color c) {
			
			((JSpinner.DefaultEditor) sporncentaje_variacion.getEditor()).getTextField().setOpaque(opac);
			((JSpinner.DefaultEditor) sporncentaje_variacion.getEditor()).getTextField().setBackground(c);
			
		}
		
		
		public int [] getAleatorio_crd() {
			
			int [] crd= {aleatorioX - spaceX, aleatorioW + spaceX};
			
			return crd;
			
		}
		
		
		public int [] [] getTrimestres_crd(){
			
			int [] [] crd=new int[4][2];
			
			for(int i=0; i<crd.length; i++) {
				
				crd[i][0]=trimestres[i].getLocation().x;
				crd[i][1]=trimestres[i].getSize().width;
				
			}
			
			return crd;
			
		}
		
		public void select_Trimestre(boolean opac, Color c, int i) {
			
			((JSpinner.DefaultEditor) valor_trimestres[i].getEditor()).getTextField().setOpaque(opac);
			((JSpinner.DefaultEditor) valor_trimestres[i].getEditor()).getTextField().setBackground(c);
			
		}
		
		
	}
	
}

class start implements Runnable{

	private archivo log;
	private archivo_resultado final_log;
	private int ejecucion;
	private int num_inversores;
	private String[] nombre_inversores;
	private int[] dinero_inversores;
	private int[] porcentaje_inversores;
	
	private int num_empresas;
	private String[] nombre_empresas;
	private int [] num_acciones, valor_max_accion, porcentaje_variacion;
	private double [] valor_acciones, valor_min_accion;
	private boolean [] prevision_aleatoria;
	private double[] [] prevision_acciones;
		
	public start (archivo _log, archivo_resultado _final_log, int _ejecucion, int _num_inversores, String[] _nombre_inversores, int[] _dinero_inversores, int[] _porcentaje_inversores, int _num_empresas, String[] _nombre_empresas, int[] _num_acciones, double[] _valor_acciones, int[] _valor_max_accion, double[] _valor_min_accion, int[] _porcentaje_variacion, boolean[] _prevision_aleatoria, double[][] _prevision_acciones) {
		
		log=_log;
		final_log=_final_log;
		ejecucion=_ejecucion;
		
		num_inversores=_num_inversores;
		nombre_inversores=_nombre_inversores;
		dinero_inversores=_dinero_inversores;
		porcentaje_inversores=_porcentaje_inversores;
		
		num_empresas=_num_empresas;
		nombre_empresas=_nombre_empresas;
		num_acciones=_num_acciones;
		valor_acciones=_valor_acciones;
		valor_max_accion=_valor_max_accion;
		valor_min_accion=_valor_min_accion;
		porcentaje_variacion=_porcentaje_variacion;
		prevision_aleatoria=_prevision_aleatoria;
		prevision_acciones=_prevision_acciones;
		
	}
	
	public void run() {

		empresa[] empresas=new empresa[num_empresas]; //ALMACENAJE DE TODAS LAS EMPRESAS
		inversor[] inversores=new inversor[num_inversores]; //ALMACENAJE DE TODOS LOS INVERSORES
						
		for(int i=0; i<num_empresas; i++) {
			
			empresas[i]=new empresa(num_acciones[i], valor_acciones[i], valor_max_accion[i], valor_min_accion[i], porcentaje_variacion[i], prevision_aleatoria[i], prevision_acciones[i], nombre_empresas[i], log);
			
			// [DECIDE CUANTO VARIARAN LAS ACCIONES EN CADA TRIMESTRE] ///////////////////////////////////////////////////////////////////////////
			
			for(int j=0; j<4; j++) {
								
				double porcentaje=(Math.random()*1000); //SOBRE UN 1000%
				
				if(Math.random()<0.5) { //DECIDE SI AUMENTARA O DISMINUIRA EL VALOR DE LA ACCION
					
					porcentaje=porcentaje*-1;
										
				}
				
				empresas[i].previsison_acciones(porcentaje, j);
				
			}
			
			log.salto();
			
			// [DECIDE CUANTO VARIARAN LAS ACCIONES EN CADA TRIMESTRE] ///////////////////////////////////////////////////////////////////////////
			
		}
		
		log.salto();
		
		for(int i=0; i<num_inversores; i++) {
			
			inversores[i]=new inversor(nombre_inversores[i], dinero_inversores[i], porcentaje_inversores[i], i, log);
			
		}
		
		// [OPERACIONES TRIMESTRALES] ///////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		for(int i=0; i<4; i++) { //CADA VUELTA SON 3 MESES
			
			log.escribir("---" + (i+1) + "º TRIMESTRE ---");
			
			// [OPERACION DE CADA INVERSOR] ////////////////////////////////////////////////////////////////////////////////////////
			
			for(inversor inversor: inversores) { //CADA INVERSOR
				
				inversor.infoInversor();
				
				// [OBTENIENDO PREVISIONES DE LAS ACCIONES] ///////////////////////////////////////
				
				double[] variaciones=new double[num_empresas];
								
				for(int e=0; e<empresas.length; e++) {
					
					variaciones[e]=empresas[e].getPrevision(i);
										
				}
								
				inversor.prevision_acciones(variaciones, empresas, inversores); //AHORA CADA INVERSOR SABE COMO VARIARAN LAS ACCIONES DE CADA EMPRESA EN EL TRIMESTRE ACTUAL
				
				// [CALCULANDO PREVISIONES DE LAS ACCIONES] ///////////////////////////////////////
								
				if(inversor.getAcciones().size()>0) { //COMPRUEBA SI TIENE ACCIONES
					
					log.escribir("- Inversor [" + inversor.getID() + "] - Se pueden vender acciones...");
					
					ArrayList<empresa> acciones_vender=new ArrayList<empresa>(); //SE ALMACENARAN LAS ACCIONES QUE VENDERA
					
					for(empresa accion: inversor.getAcciones()) { //CADA ACCION DEL INVERSOR PUEDE VENDERLA O QUEDARSELA
						
						log.escribir("- Inversor [" + inversor.getID() + "] - Decidiendo si vender o quedarse con la accion de Empresa[" + accion.getID() + "]...");
						
						if(Math.random()>0.5) { //VENDE ACCION
							
							log.escribir("- Inversor [" + inversor.getID() + "] - Accion para vender...");
							
							acciones_vender.add(accion);
							
						}else { //SE QUEDA ACCION
							
							log.escribir("- Inversor [" + inversor.getID() + "] - Quedandose accion...");
							
						}
						
					}
					
					log.escribir("- Inversor [" + inversor.getID() + "] - Vendiendo acciones...");
					
					for(empresa accion: acciones_vender) { //VENDIENDO LAS ACCIONES
						
						inversor.vender_accion(accion);
						
					}
					
				}else {
					
					log.escribir("- Inversor [" + inversor.getID() + "] - No hay acciones que vender...");
					
				}
				
				inversor.comprar_todas_acciones(empresas); //CADA INVERSOR COMPRARA LAS MEJORES ACCIONES EN FUNCION SU % DE ACIERTO
								
			}
			
			// [OPERACION DE CADA INVERSOR] ////////////////////////////////////////////////////////////////////////////////////////
			
			for(empresa empresa: empresas) {
				
				log.salto();
				
				empresa.aplicar_valor_acciones();
				
				empresa.aplicar_prevision(i);
				
				empresa.reiniciar_porcentaje_accion();
				
				log.escribir("- Empresa[" + empresa.getID() + "] - Actualizando...");
				
			}
			
		}
		
		// [OPERACIONES TRIMESTRALES] ///////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// [RESULTADOS] //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		log.salto();
		log.salto();
		log.salto();
		log.salto();
		log.escribir("--- RESULTADOS ---");
		log.salto();
		
		ArrayList<inversor> orden_inversor=new ArrayList<inversor>(); //SE ALMAZENARAN DE FORMA ORDENADA LOS INVERSORES EN FUNCION DE SU DINERO FINAL
		
		for(inversor i: inversores) { //CADA INVERSOR VENDERA TODAS SUS ACCIONES
			
			orden_inversor.add(i);
			
			log.salto();
			
			i.infoInversor();
			
			i.vender_todas_acciones();
			
			log.escribir("---------------------------------------------------------------------------------------------");
			
			i.infoInversor();
			
			log.escribir("---------------------------------------------------------------------------------------------");
			
		}
		
		for(empresa e: empresas) {
			
			e.infoEmpresa();
			
		}
				
		Collections.sort(orden_inversor, new Comparator<inversor>() { //ORDENA LA LISTA

			public int compare(inversor arg0, inversor arg1) {
				
				if(arg0.getDinero()>arg1.getDinero()) {
					
					return -1;
					
				}else if(arg0.getDinero()<arg1.getDinero()) {
					
					return 1;
					
				}else {
					
					return 0;
					
				}
			}
			
		});
		

		for(int i=0; i<orden_inversor.size(); i++) { //REGISTRA LA POSICION INDIVIDUAL DE CADA INVERSOR
			
			inversor inversor=orden_inversor.get(i);
			
			inversor.setPosicion(ejecucion, inversor.getNum_inversor(), i);
			
		}
		
		final_log.escribir(ejecucion, inversores);
		
		// [RESULTADOS] //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		log.close();
		
				
	}
	
}

class empresa{
	
	private int acciones; //Nº DE ACCIONES RESTANTES
	private double valor_acciones; //PRECIO DE LAS ACCIONES
	private double valor_max_accion, valor_min_accion;
	private double porcentaje_accion; //EN PORCENTAJE LO QUE SUBIRA O BAJARA LAS ACCIONES DESPUES DE QUE TODOS LOS INVERSORES HAYAN COMPRADO/VENDIDO. +[porcentaje_variacion]% POR CADA COMPRA / -[porcentaje_variacion]% POR CADA VENTA
	private double[] prevision_acciones; //PORCENTAJE DE VARIACION PREVISTO EN CADA TRIMESTRE
	private boolean prevision_acciones_aleatorio; //APLICAR O NO PREVISIONES ALEATORIAS
	private double porcentaje_variacion; //PORCENTAJE QUE VARIARAN LAS EMPRESAS DESPUÉS DE COMPRAR/VENDER ACCIONES
	private final String ID;
	
	private archivo log;
			
		public empresa(int _acciones, double _valor_acciones, int _valor_max_accion, double _valor_min_accion, int _porcentaje_variacion, boolean prevision_aleatoria, double[] _prevision_acciones, String id, archivo archivo) {
		
		acciones=_acciones;
		valor_acciones=_valor_acciones;
		valor_max_accion=_valor_max_accion;
		valor_min_accion=_valor_min_accion;
		porcentaje_accion=0;
		porcentaje_variacion=_porcentaje_variacion;
		prevision_acciones_aleatorio=prevision_aleatoria;
		prevision_acciones=new double[4];

		ID=id;
			
		log=archivo;
		
		infoEmpresa();
		
		if(!prevision_acciones_aleatorio) {
			
			prevision_acciones=_prevision_acciones;
						
			for(int i=0; i<prevision_acciones.length; i++) {
							
				if(prevision_acciones[i]>0) {
					
					log.escribir("Empresa [" + ID + "] en el " + (i+1) + "º trimestre aumentara un " + (prevision_acciones[i]) + "%.");
					
				}else {
																				
					log.escribir("Empresa [" + ID + "] en el " + (i+1) + "º trimestre disminuira un " + (prevision_acciones[i]) + "%.");
					
				}
			}
			
		}
		
	}

	
	public void valor_acciones(double porcentaje) { //SUBE O BAJA EL VALOR DE LAS ACCIONES
		
		valor_acciones=valor_acciones + valor_acciones*(porcentaje/100); //VALOR DE LAS ACCIONES APLICANDO EL PORCENTAJE
				
		if(porcentaje>=0) {
			log.escribir("Empresa[" + ID + "]: Las acciones suben un " + porcentaje + "% --> Accion: " + valor_acciones + " rupias");
		}else {
			log.escribir("Empresa[" + ID + "]: Las acciones bajan un " + porcentaje + "% --> Accion: " + valor_acciones + " rupias");
		}
				
		if(valor_acciones>valor_max_accion) { //VALOR DE LAS ACCIONES LIMITADO A 100
			
			valor_acciones=valor_max_accion;
			
			log.escribir("Empresa[" + ID + "]: Valor maximo superado, rebajando a " + valor_max_accion + " rupias");
			
		}else if(valor_acciones<valor_min_accion) { //VALOR DE LAS ACCIONES LIMITADO A valor_max_accion
			
			valor_acciones=valor_min_accion;

			log.escribir("Empresa[" + ID + "]: Valor minimo superado, elevando a " + valor_min_accion + " rupias");
			
		}
		
	}
	
	public void previsison_acciones(double porcentaje, int trimestre) { //ALMACENA LA VARIACION DE LAS ACCIONES DE UN TRIMESTRE
						
		if(prevision_acciones_aleatorio) {
			
			if(porcentaje>0) {
				
				log.escribir("Empresa [" + ID + "] en el " + (trimestre+1) + "º trimestre aumentara un " + porcentaje + "%.");
				
			}else {
				
				log.escribir("Empresa [" + ID + "] en el " + (trimestre+1) + "º trimestre disminuira un " + porcentaje + "%.");
				
			}
						
			prevision_acciones[trimestre]=porcentaje;
			
		}
				
	}
	
	public void aplicar_prevision(int trimestre) { //APLICA LA VARIACION TRIMESTRAL DE LAS ACCIONES PREVISTAS
	
		log.escribir("Empresa [" + ID + "] - Aplicando un " + prevision_acciones[trimestre] + "% a las acciones como estava previsto en el trimestre " + (trimestre + 1));
		
		valor_acciones(prevision_acciones[trimestre]);
		
	}

	
	public void comprar_accion() { //SE COMPRA UNA ACCION
		
		porcentaje_accion+=porcentaje_variacion;
		
		acciones--;
				
		if(porcentaje_accion>=0) {
			
			log.escribir("Empresa[" + ID + "]: ACCION COMPRADA. Valor acciones +" + porcentaje_accion + "%. Acciones restantes: " + acciones);
			
		}else {
			
			log.escribir("Empresa[" + ID + "]: ACCION COMPRADA. Valor acciones " + porcentaje_accion + "%. Acciones restantes: " + acciones);
			
		}
		
	}
	
	public void vender_accion() { //SE VENDE UNA ACCION
		
		porcentaje_accion-=porcentaje_variacion;
		
		acciones++;
				
		if(porcentaje_accion>=0) {
			
			log.escribir("Empresa[" + ID + "]: ACCION VENDIDA. Valor acciones +" + porcentaje_accion + "%. Acciones restantes: " + acciones);
			
		}else {
			
			log.escribir("Empresa[" + ID + "]: ACCION VENDIDA. Valor acciones " + porcentaje_accion + "%. Acciones restantes: " + acciones);
			
		}
				
	}
	
	public void aplicar_valor_acciones() { //HARA SUBIR O BAJAR EL VALOR DE LAS ACCIONES DESPUES DE QUE TODOS LOS INVERSORES HAYAN COMPRADO O VENDIDO LAS ACCIONES
		
		log.escribir("Empresa[" + ID + "]: Aplicando un " + porcentaje_accion + "% a las acciones por la compra/venda");
		
		valor_acciones(porcentaje_accion);
				
	}
	
	public void reiniciar_porcentaje_accion() {
		
		porcentaje_accion=0;
		
	}
	
	public int getAcciones() {
		
		return acciones;
	}
	
	public double getValor_acciones() {
		
		return valor_acciones;
	}
	
	public double getPrevision(int trimestre) {
		
		return prevision_acciones[trimestre];
		
	}
	
	public double getValor_max_accion() {
		
		return valor_max_accion;
		
	}
	
	public double getValor_min_accion() {
		
		return valor_min_accion;
		
	}
	
	public String getID() {
		
		return ID;
		
	}
	
	public void infoEmpresa() {
		
		log.escribir("Empresa[" + ID + "] - Nº acciones: " + acciones + " - Valor acciones: " + valor_acciones + " rupias");
		
	}
	
}

class inversor{ //INVERSOR PADRE
	
	private double dinero; //DINERO
	private int n_acciones; //Nº DE ACCIONES QUE CONTIENE
	private ArrayList<empresa> acciones; //LISTA DE LAS ACCIONES QUE CONTIENE
	private ArrayList<empresa> prevision_acciones; //LISTA QUE CONTIENE LAS ACCIONES A INVERTIR ORDENADA EN FUNCION DE LA GANANCIA MINIMA PREVISTA
	private final String ID;
	private int porcentaje_acierto;
	private int num_inversor;
	
	private archivo log;
	
	public inversor(String id, int _dinero, int _porcentaje_acierto, int _num_inversor, archivo archivo) {
		
		dinero=_dinero;
		n_acciones=0;
		acciones=new ArrayList<empresa>();
		porcentaje_acierto=_porcentaje_acierto;
		num_inversor=_num_inversor;
		ID=id;
		
		log=archivo;
		
		infoInversor();
		log.escribir("Inversor[" + ID + "]: Probabilidad de acierto: " + porcentaje_acierto + "%");
		
	}
	
	public boolean comprar_accion(empresa e) { //COMPRA ACCION ESPECIFICA
				
		if(dinero>=e.getValor_acciones()) {
									
			if(e.getAcciones()>0) { //COMPRUEBA QUE LA EMPRESA LE QUEDEN ACCIONES PARA VENDER
				dinero-=e.getValor_acciones();
				e.comprar_accion();
				acciones.add(e);
				n_acciones++;
							
				log.escribir("Inversor[" + ID + "]: COMPRANDO ACCION - Empresa[" + e.getID() + "] - Valor de la accion: " + e.getValor_acciones() + " rupias - Dinero restante: " + dinero + " - Nº de acciones actuales: " + n_acciones);
				
			}else {
				
				log.escribir("Inversor[" + ID + "]: ACCIONES INSUFICIENTES");
				
				return false;
				
			}
			
			return true;
						
		}else {
			
			log.escribir("Inversor[" + ID + "]: COMPRANDO ACCION - Empresa[" + e.getID() + "] - Valor de la accion: " + e.getValor_acciones() + " rupias - DINERO INSUFICIENTE");
			
			return false;
		}
		
	}
	
	public boolean vender_accion(empresa e) { //VENDE ACCION
				
		if(n_acciones>0) {
						
			dinero+=e.getValor_acciones();
			e.vender_accion();
			acciones.remove(e);
			n_acciones--;
						
			log.escribir("Inversor [" + ID + "]: VENDIENDO ACCION - Empresa[" + e.getID() + "] - Valor de la accion: " + e.getValor_acciones() + " rupias - Dinero actual: " + dinero + " rupias -  Nº de acciones restantes: " + n_acciones);
			
			return true;
			
		}else {
			
			log.escribir("Inversor [" + ID + "]: VENDIENDO ACCION - Empresa[" + e.getID() + "] - NO HAY ACCIONES PARA VENDER");
			
			return false;
			
		}
	}
	
	public void vender_todas_acciones() { //VENDE TODAS LAS ACCIONES QUE CONTENGA
				
		log.escribir("Inversor [" + ID + "]: VENDIENDO TODAS LAS ACCIONES");
		
		ArrayList<empresa> acciones_vender=new ArrayList<empresa>();
		
		for(empresa e: acciones) {
			acciones_vender.add(e);
		}
		
		
		for(empresa accion: acciones_vender) {
			
			vender_accion(accion);
			
		}
		
	}
	
	public void comprar_todas_acciones(empresa[] empresas) {
		
		double dinero_total=getDinero();
		
		double dinero_acierto=dinero_total*(porcentaje_acierto/100.0); //DINERO QUE INVERTIRA EN LAS MEJORES ACCIONES (70%)
		
		double dinero_aleatorio=dinero_total-dinero_acierto; //DINERO QUE INVERTIRA EN ACCIONES ALEATORIAS (30%)
				
		if(dinero_acierto>0) {
						
			ArrayList<empresa> prevision=getPrevisiones(); //OBTIENE LA LISTA ORDENADA DE MEJORES ACCIONES A INVERTIR
			
			Iterator<empresa> it=prevision.iterator();
			
			empresa accion=it.next();
					
			log.escribir("Inversor [" + getID() + "] - Comprando las mejores acciones con " + dinero_acierto + " rupias de " + dinero_total + " rupias en total.");
		
			while(it.hasNext()) {
				
				if(dinero_acierto>=accion.getValor_acciones()) { //COMPRARA TODAS LAS ACCIONES QUE PUEDA DE LA EMPRESA ACTUAL

					log.escribir("Inversor [" + getID() + "] - Puede comprar la accion de Empresa[" + accion.getID() + "]");
					
					dinero_acierto-=accion.getValor_acciones();
					
					if(!comprar_accion(accion)) {
						
						log.escribir("Inversor [" + getID() + "] - No puede comprar la accion de Empresa[" + accion.getID() + "]");
						
						accion=it.next();
						
					}
					
				}else { //CUANDO NO PUEDA COMPRAR MAS ACCIONES DE ESA EMPRESA, COMPRAR ACCIONES DE LA SIGUIENTE EMPRESA QUE LE PERMITA SIGUIENO EL ORDEN
					
					log.escribir("Inversor [" + getID() + "] - No puede comprar la accion de Empresa[" + accion.getID() + "]");
					
					accion=it.next();
					
				}
				
			}
		}
		
		if(dinero_aleatorio>0) {
		
			log.escribir("Inversor [" + getID() + "] - Comprando acciones aleatorias con " + dinero_aleatorio + " rupias de " + dinero_total + " rupias en total.");
			
			ArrayList<empresa> posibles_acciones; //LISTA DE ACCIONES ALEATORIAS QUE PUEDE COMPRAR CON LA RESTA DE DINERO
			
			// [COMPRANDO TODAS LAS ACCIONES QUE PUEDA] /////////////////////////////////////////////////
			
			do {
				
				posibles_acciones=new ArrayList<empresa>();
								
				for(empresa acciones: empresas) { //BUSCA TODAS LAS ACCIONES QUE PUEDE COMPRAR
					
					if(dinero_aleatorio>=acciones.getValor_acciones()&&acciones.getAcciones()>0) {
						
						posibles_acciones.add(acciones);
						
					}
					
				}
				
				log.escribir("- Inversor [" + getID() + "] - A " + posibles_acciones.size() + " empresas que puede comprar acciones...");

				int aleatorio=(int) (Math.random()*100);

				if(posibles_acciones.size()>0) {
								
					double dividendo=(double)100/posibles_acciones.size();
									
					int n_accion=(int)(aleatorio/dividendo); //ELIJE UNA POSICION ALEATORIA DE 'posibles_acciones'
					
				//	if(comprar_accion(posibles_acciones.get(n_accion))) {
						
						dinero_aleatorio-=posibles_acciones.get(n_accion).getValor_acciones();
						comprar_accion(posibles_acciones.get(n_accion));
						
				//	}
																				
				}
												
				}while(posibles_acciones.size()>0);
			
				log.escribir("- Inversor [" + getID() + "] - Ya ha comprado todas las acciones que podia...");
		}
		
		// [COMPRANDO TODAS LAS ACCIONES QUE PUEDA] /////////////////////////////////////////////////
			
	}
	
	public void prevision_acciones(double[] variaciones, empresa[] empresas, inversor[] inversores) { //CALCULA LA GANANCIA MINIMA DE CADA ACCION TENIENDO EN CUENTA LAS PREVISIONES Y LA COMPRA/VENTA DEL RESTO DE INVERSORES
		
		// [CLASE INTERNA LOCAL] ///////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		class prevision_accion{ //PAQUETE DONDE SE ALMACENARA TODA LA INFORMACION DE CADA ACCION
			
			private empresa empresa;
			private double valor_medio, valor_maximo, valor_minimo, valor_accion, porcentaje, ganancia;
			
			public prevision_accion(empresa _empresa, double _valor_medio, double _valor_maximo, double _valor_minimo, double _valor_accion, double _porcentaje) {
				
				empresa=_empresa; //ACCION
				valor_medio=_valor_medio; //VALOR DE LA ACCION APLICANDO SOLO LA PREVISION
				valor_maximo=_valor_maximo; //VALOR MAXIMO QUE PODRA VALER LA ACCIONES
				valor_minimo=_valor_minimo; //VALOR MINIMO QUE PODRA VALE LA ACCION
				valor_accion=_valor_accion; //VALOR DE LA ACCION ACTUALMENTE
				porcentaje=_porcentaje; //PORCENTAJE PREVISTO QUE SE LE APLICARA A LA ACCION
				
				ganancia=valor_minimo-valor_accion; //GANANCIA MINIMA DE LA ACCION
				
			}
			
			public empresa getEmpresa() {
				
				return empresa;
				
			}
			
			public double getGanancia() {
				
				return ganancia;
				
			}

			public double getValor_medio() {
				
				return valor_medio;
				
			}

			public double getValor_maximo() {
				
				return valor_maximo;
				
			}

			public double getValor_minimo() {
				
				return valor_minimo;
				
			}
			
			public double getValor_accion() {
				
				return valor_accion;
				
			}
			
			public double getPorcentaje() {
				
				return porcentaje;
				
			}
			
		}
		
		// [CLASE INTERNA LOCAL] ///////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		double[] valor_maximo=new double[variaciones.length]; //VALOR MAXIMO QUE PODRAN TENER TODAS LAS ACCIONES
		double[] valor_minimo=new double[variaciones.length]; //VALOR MINIMO QUE PODRAN TENER TODAS LAS ACCIONES
		double[] valor_medio=new double[variaciones.length]; //VALOR DE TODAS LAS ACCIONES SI NADIE COMPRA/VENDE
		
		// [RECOGE A LOS OTROS DOS INVERSORES] //////////////////////////////////////////////////////////////////////////////////////////
		
//		inversor inversor1=null; 
//		inversor inversor2=null;
		
		ArrayList<inversor> lista_resto_inversores=new ArrayList<inversor>(); //LISTA DEL RESTO DE INVERSORES
		
		for(inversor inversor: inversores) {
										
			if(inversor.getID()!=this.getID()) { //EVALUA QUE NO SE AÑADA A SI MISMO
					
				lista_resto_inversores.add(inversor);
					
			}
				
		}
		
		inversor[] resto_inversores=new inversor[lista_resto_inversores.size()]; //ARRAY DONDE SE ALMACENARAN TODOS LOS DEMAS INVERSORES
		resto_inversores=lista_resto_inversores.toArray(resto_inversores);
				
//		ArrayList<empresa> acciones1=null; //LISTA DE TODAS LAS ACCIONES QUE CONTIENE CADA INVERSOR
//		ArrayList<empresa> acciones2=null;
		
		@SuppressWarnings("unchecked")
		ArrayList<empresa> [] acciones_inversores=new ArrayList [lista_resto_inversores.size()]; //ARRAY DONDE SE ALMACENARAN LAS LISTAS DE LAS ACCIONES DE CADA INVERSOR
		
//		double dinero1_max; //DINERO MAXIMO QUE PUEDE LLEGAR A TENER LOS INVERSORES
//		double dinero2_max;
		
		double[] dinero_max=new double[lista_resto_inversores.size()]; //ARRAY DONDE SE ALMACENARA EL DINERO DE LOS INVERSORES
		
		/*
		boolean inversor1_compl=false;
		
		for(inversor inversor: inversores) {
						
			if(inversor.getID()!=getID()) {
				
				if(!inversor1_compl) {
					
					inversor1=inversor;
					
					inversor1_compl=true;
					
				}else {
										
					inversor2=inversor;
					
				}
				
				
			}
			
		}*/
		
		// [DINERO QUE MAXIMO QUE TENDRIAN EN EL CASO DE QUE LOS OTROS INVERSORES VENDIERAN TODAS LAS ACCIONES POSSIBLES] ////////
		
		/*
		dinero1_max=inversor1.getDinero();
		
		if(inversor1.getNAcciones()>0) {
			
			acciones1=inversor1.getAcciones();
			
			for(empresa accion: acciones1) {
				
				dinero1_max+=accion.getValor_acciones();
				
			}	
		}
				
		dinero2_max=inversor2.getDinero();
		
		if(inversor2.getNAcciones()>0) {
			
			acciones2=inversor2.getAcciones();
			
			for(empresa accion: acciones2) {
				
				dinero2_max+=accion.getValor_acciones();
				
			}
			
		}
		*/
		
		for(int i=0; i<lista_resto_inversores.size(); i++) {
			
			inversor inversor=resto_inversores[i];
			
			double dinero_max_inversor=inversor.getDinero();
			
			ArrayList<empresa> acciones_inversor=null;
			
			if(inversor.getNAcciones()>0) {
				
				acciones_inversor=inversor.getAcciones();
				
				for(empresa accion: acciones_inversor) {
					
					dinero_max_inversor+=accion.getValor_acciones();
					
				}
				
				acciones_inversores[i]=acciones_inversor;
				
			}
			
			dinero_max[i]=dinero_max_inversor;
			
		}

		// [DINERO QUE MAXIMO QUE TENDRIAN EN EL CASO DE QUE LOS OTROS INVERSORES VENDIERAN TODAS LAS ACCIONES POSSIBLES] ////////
		
		// [RECOGE A LOS OTROS DOS INVERSORES] //////////////////////////////////////////////////////////////////////////////////////////

		
		// [CALCULA LAS ACCIONES MAXIMAS QUE PUEDE COMPRAR/VENDER DE CADA EMPRESA Y COMO AFECTARIA A ESTAS] /////////////////////////////
		
		for(int i=0; i<variaciones.length; i++) { //CADA VUELTA CALCULA LAS ACCIONES DE UNA EMPRESA EN CONCRETO
			
			double valor_accion=empresas[i].getValor_acciones() + empresas[i].getValor_acciones()*(variaciones[i]/100.0); //VALOR DE LA ACCION DESPUES DE APLICAR EL PORCENTAJE PREVISTO
			
			//////////////////////////////////////////////////////
			
			//int n_compras1=(int) (dinero1_max/empresas[i].getValor_acciones()); //EL Nº MAXIMO DE ACCIONES QUE PUEDE COMPRAR DE CADA ACCION
			//int n_compras2=(int) (dinero2_max/empresas[i].getValor_acciones());
			
			//int n_ventas1=0;
			//int n_ventas2=0;
			
			/*if(inversor1.getNAcciones()>0) { //SI EL VENDIERA TODAS LAS ACCIONES QUE TIENE DE LA EMPRESA EN CONCRETO
				
				for(empresa accion: acciones1) {
					
					if(accion==empresas[i]) {
						
						n_ventas1--;
						
					}
				}
			}
			
			if(inversor2.getNAcciones()>0) {
				
				for(empresa accion: acciones2) {
					
					if(accion==empresas[i]) {
						
						n_ventas2--;
						
					}
					
				}
				
			}*/
			
			int n_compras=0;
			int n_ventas=0;
			
			for(int j=0; j<lista_resto_inversores.size(); j++) {
				
				inversor inversor=resto_inversores[j];
				
				n_compras=(int) (dinero_max[j]/empresas[i].getValor_acciones());
				
				n_ventas=0;
				
				if(inversor.getNAcciones()>0) {
					
					for(empresa accion: acciones_inversores[j]) {
						
						if(accion==empresas[i]) {
							
							n_ventas--;
							
						}
						
					}
					
				}
				
			}
			
			/////////////////////////////////////////////
			
			n_compras+=n_ventas;
			
			// [VALOR MAXIMO, MINIMO Y MEDIO DE CADA ACCION] //////////////////////////////////////////////////////////
			
			double valor_max=empresas[i].getValor_acciones() + empresas[i].getValor_acciones()*(((n_compras)*20.0)/100.0); //VALOR MAXIMO QUE ALCANZARIAN LAS ACCIONES SI LOS DOS INVERSORES COMPRARAN TODAS LAS ACCIONES DE LA EMPRESA EN CONCRETO
			double valor_min=empresas[i].getValor_acciones() + empresas[i].getValor_acciones()*(((n_ventas)*20.0)/100.0); //VALOR MINIMO QUE ALCANZARIAN LAS ACCIONES SI LOS DOS INVERSORES VENDIERA TODAS LAS ACCIONES QUE TUVIERAN DE LA EMPRESA EN CONCRETO
									
			valor_max=valor_max + valor_max*(variaciones[i]/100.0); //APLICA EL PORCENTAJE PREVISTO SOBRE EL VALOR MAXIMO DESPUES DE LAS COMPRAS
			valor_min=valor_min + valor_min*(variaciones[i]/100.0); //APLICA EL PORCENTAJE PREVISTO SOBRE EL VALOR MINIMO DESPUES DE LAS VENTAS
			
			// [LIMITLAS LOS VALORES A 100 O 0.5] //////////////////////////////////
			if(valor_accion>empresas[i].getValor_max_accion()) { valor_accion=empresas[i].getValor_max_accion(); }
			if(valor_accion<empresas[i].getValor_min_accion()) { valor_accion=empresas[i].getValor_min_accion(); }
			
			if(valor_max>empresas[i].getValor_max_accion()) { valor_max=empresas[i].getValor_max_accion(); }
			if(valor_max<empresas[i].getValor_min_accion()) { valor_max=empresas[i].getValor_min_accion(); }
			
			if(valor_min>empresas[i].getValor_max_accion()) { valor_min=empresas[i].getValor_max_accion(); }
			if(valor_min<empresas[i].getValor_min_accion()) { valor_min=empresas[i].getValor_min_accion(); }
			
			// [LIMITLAS LOS VALORES A 100 O 0.5] //////////////////////////////////

			//GUARDA LOS VALORES PREVISTOS
			valor_medio[i]=valor_accion; 
			valor_maximo[i]=valor_max;
			valor_minimo[i]=valor_min;			
			
			// [VALOR MAXIMO, MINIMO Y MEDIO DE CADA ACCION] //////////////////////////////////////////////////////////
			
		}
		
		// [CALCULA LAS ACCIONES MAXIMAS QUE PUEDE COMPRAR/VENDER DE CADA EMPRESA Y COMO AFECTARIA A ESTAS] /////////////////////////////
		
		log.escribir("Inversor [" + ID + "] - Prevision de acciones:");
		
		
		ArrayList<prevision_accion> previsiones=new ArrayList<prevision_accion>(); //LISTA DEL PAQUETE DE PREVISIONES DE CADA ACCION
		
		for(int i=0; i<valor_medio.length; i++) { //CREA TODOS LOS PAQUETES DE CADA ACCION CON TODA LA INFORMACION CALCULADA
			
			prevision_accion prevision=new prevision_accion(empresas[i], valor_medio[i], valor_maximo[i], valor_minimo[i], empresas[i].getValor_acciones(), variaciones[i]);		
			previsiones.add(prevision);
			
		}
		
		Collections.sort(previsiones, new Comparator<prevision_accion>() { //ORDENA LA LISTA EN FUNCION DE LA GANANCIA MINIMA, DE MAYOR A MENOR

			public int compare(prevision_accion arg0, prevision_accion arg1) {
				
				if(arg0.getGanancia()>arg1.getGanancia()) {
					
					return -1;
					
				}else if(arg0.getGanancia()<arg1.getGanancia()) {
					
					return 1;
					
				}else {
					
					return 0;
					
				}
			}
			
		});
		
		prevision_acciones=new ArrayList<empresa>();
		
		for(prevision_accion prevision: previsiones) { //ALMACENA EN LA LISTA LAS ACCIONES EN EL ORDEN A INVERTIR
			
			prevision_acciones.add(prevision.getEmpresa());
			
			log.escribir("    - Empresa: " + prevision.getEmpresa().getID() + " - Ganancia mínima: " + prevision.getGanancia() + " rupias --> Valor medio: " + prevision.getValor_medio() + " - Valor max: " + prevision.getValor_maximo() + " - Valor min: " + prevision.getValor_minimo() + " Valor act: " + prevision.getValor_accion() + " - Variacion: " + prevision.getPorcentaje() + "%");
						
		}						
		
	}
	
	public void setPosicion(int ejecucion, int numero_inversor, int posicion) {
		
		main_TDR_Leo.setPosicion(ejecucion, numero_inversor, posicion);
		
	}
	
	public int getNum_inversor() {
		
		return num_inversor;
		
	}
	
	public ArrayList<empresa> getPrevisiones() {
		
		return prevision_acciones;
		
	}
	
	public ArrayList<empresa> getAcciones(){
		
		return acciones;
		
	}
	
	public double getDinero() {
		
		return dinero;
		
	}
	
	public int getNAcciones() {
		
		return n_acciones;
		
	}
	
	public String getID() {
		
		return ID;
		
	}
	
	public void infoInversor() {
		
		log.escribir("Inversor [" + ID + "] - Dinero: " + dinero + " rupias - Nº acciones: " + n_acciones + " - Lista de acciones:");
		
		for(empresa accion: acciones) {
			
			log.escribir("    - Empresa[" + accion.getID() + "]");
			
		}
		
	}
	
	
}

/*
class profesional extends inversor{ //INVERSOR PROFESIONAL
	
	private archivo log;
	
	public profesional(String id, archivo archivo) {
		
		super(id, archivo);
		
		log=archivo;
		
		log.escribir("[" + id + "] - Probabilidad de acierto: 70%");
	}

	// [CALCULA COMPRAR LAS MEJORES ACCIONES EN FUNCION DE SU PORCENTAJE DE ACIERTO (70%) ] /////////////////////////
	
	public void comprar_todas_acciones(empresa[] empresas) {
		
		double dinero_total=super.getDinero();
		
		double dinero_acierto=dinero_total*0.7; //DINERO QUE INVERTIRA EN LAS MEJORES ACCIONES (70%)
		
		double dinero_aleatorio=dinero_total*0.3; //DINERO QUE INVERTIRA EN ACCIONES ALEATORIAS (30%)
		
		ArrayList<empresa> prevision=super.getPrevisiones(); //OBTIENE LA LISTA ORDENADA DE MEJORES ACCIONES A INVERTIR
		
		Iterator<empresa> it=prevision.iterator();
		
		empresa accion=it.next();
				
		log.escribir("Inversor [" + super.getID() + "] - Comprando las mejores acciones con " + dinero_acierto + " rupias de " + dinero_total + " rupias en total.");
		
		while(it.hasNext()) {
					
			if(dinero_acierto>=accion.getValor_acciones()) { //COMPRARA TODAS LAS ACCIONES QUE PUEDA DE LA EMPRESA ACTUAL

				log.escribir("Inversor [" + super.getID() + "] - Puede comprar la accion de Empresa[" + accion.getID() + "]");
				
				dinero_acierto-=accion.getValor_acciones();
				
				super.comprar_accion(accion);
				
			}else { //CUANDO NO PUEDA COMPRAR MAS ACCIONES DE ESA EMPRESA, COMPRAR ACCIONES DE LA SIGUIENTE EMPRESA QUE LE PERMITA SIGUIENO EL ORDEN
				
				log.escribir("Inversor [" + super.getID() + "] - No puede comprar la accion de Empresa[" + accion.getID() + "]");
				
				accion=it.next();
				
			}
			
		}
		
		log.escribir("Inversor [" + super.getID() + "] - Comprando acciones aleatorias con " + dinero_aleatorio + " rupias de " + dinero_total + " rupias en total.");
		
		ArrayList<empresa> posibles_acciones; //LISTA DE ACCIONES ALEATORIAS QUE PUEDE COMPRAR CON LA RESTA DE DINERO
		
		// [COMPRANDO TODAS LAS ACCIONES QUE PUEDA] /////////////////////////////////////////////////
		
		do {
			
			posibles_acciones=new ArrayList<empresa>();
							
			for(empresa acciones: empresas) { //BUSCA TODAS LAS ACCIONES QUE PUEDE COMPRAR
				
				if(dinero_aleatorio>=acciones.getValor_acciones()) {
					
					posibles_acciones.add(acciones);
					
				}
				
			}
			
			log.escribir("- Inversor [" + super.getID() + "] - A " + posibles_acciones.size() + " empresas que puede comprar acciones...");

			int aleatorio=(int) (Math.random()*100);

			if(posibles_acciones.size()>0) {
							
				double dividendo=(double)100/posibles_acciones.size();
								
				int n_accion=(int)(aleatorio/dividendo); //ELIJE UNA POSICION ALEATORIA DE 'posibles_acciones'
												
				dinero_aleatorio-=posibles_acciones.get(n_accion).getValor_acciones();
				
				super.comprar_accion(posibles_acciones.get(n_accion));
									
			}
											
			}while(posibles_acciones.size()>0);
		
			log.escribir("- Inversor [" + super.getID() + "] - Ya ha comprado todas las acciones que podia...");
		
		// [COMPRANDO TODAS LAS ACCIONES QUE PUEDA] /////////////////////////////////////////////////
			
	}
	
	// [CALCULA COMPRAR LAS MEJORES ACCIONES EN FUNCION DE SU PORCENTAJE DE ACIERTO] /////////////////////////
		
	public void setPosicion(int ejecucion, int posicion) {
		
		main_TDR_Leo.set_posicion_pro(ejecucion, posicion);
		
	}
	
}

class estudiante extends inversor{ //INVERSOR ESTUDIANTE
	
	private archivo log;
	
	public estudiante (String id, archivo archivo) {
				
		super(id, archivo);
	
		log=archivo;
		
		log.escribir("[" + id + "] - Probabilidad de acierto: 50%");
		
	}

	// [CALCULA COMPRAR LAS MEJORES ACCIONES EN FUNCION DE SU PORCENTAJE DE ACIERTO (50%) ] /////////////////////////
	
	public void comprar_todas_acciones(empresa[] empresas) {
		
		double dinero_total=super.getDinero();
		
		double dinero_acierto=dinero_total*0.5; //DINERO QUE INVERTIRA EN LAS MEJORES ACCIONES (50%)
		
		double dinero_aleatorio=dinero_total*0.5; //DINERO QUE INVERTIRA EN ACCIONES ALEATORIAS (50%)
		
		ArrayList<empresa> prevision=super.getPrevisiones(); //OBTIENE LA LISTA ORDENADA DE MEJORES ACCIONES A INVERTIR
		
		Iterator<empresa> it=prevision.iterator();
		
		empresa accion=it.next();
				
		log.escribir("Inversor [" + super.getID() + "] - Comprando las mejores acciones con " + dinero_acierto + " rupias de " + dinero_total + " rupias en total.");
		
		while(it.hasNext()) {
					
			if(dinero_acierto>=accion.getValor_acciones()) { //COMPRARA TODAS LAS ACCIONES QUE PUEDA DE LA EMPRESA ACTUAL

				log.escribir("Inversor [" + super.getID() + "] - Puede comprar la accion de Empresa[" + accion.getID() + "]");
				
				dinero_acierto-=accion.getValor_acciones();
				
				super.comprar_accion(accion);
				
			}else { //CUANDO NO PUEDA COMPRAR MAS ACCIONES DE ESA EMPRESA, COMPRAR ACCIONES DE LA SIGUIENTE EMPRESA QUE LE PERMITA SIGUIENO EL ORDEN
				
				log.escribir("Inversor [" + super.getID() + "] - No puede comprar la accion de Empresa[" + accion.getID() + "]");
				
				accion=it.next();
				
			}
			
		}
		
		log.escribir("Inversor [" + super.getID() + "] - Comprando acciones aleatorias con " + dinero_aleatorio + " rupias de " + dinero_total + " rupias en total.");
		
		ArrayList<empresa> posibles_acciones; //LISTA DE ACCIONES ALEATORIAS QUE PUEDE COMPRAR CON LA RESTA DE DINERO
		
		// [COMPRANDO TODAS LAS ACCIONES QUE PUEDA] /////////////////////////////////////////////////
		
		do {
			
			posibles_acciones=new ArrayList<empresa>();
							
			for(empresa acciones: empresas) { //BUSCA TODAS LAS ACCIONES QUE PUEDE COMPRAR
				
				if(dinero_aleatorio>=acciones.getValor_acciones()) {
					
					posibles_acciones.add(acciones);
					
				}
				
			}
			
			log.escribir("- Inversor [" + super.getID() + "] - A " + posibles_acciones.size() + " empresas que puede comprar acciones...");

			int aleatorio=(int) (Math.random()*100);

			if(posibles_acciones.size()>0) {
							
				double dividendo=(double)100/posibles_acciones.size();
								
				int n_accion=(int)(aleatorio/dividendo); //ELIJE UNA POSICION ALEATORIA DE 'posibles_acciones'
												
				dinero_aleatorio-=posibles_acciones.get(n_accion).getValor_acciones();
				
				super.comprar_accion(posibles_acciones.get(n_accion));
									
			}
											
			}while(posibles_acciones.size()>0);
		
			log.escribir("- Inversor [" + super.getID() + "] - Ya ha comprado todas las acciones que podia...");
		
		// [COMPRANDO TODAS LAS ACCIONES QUE PUEDA] /////////////////////////////////////////////////
			
	}
		
	// [CALCULA COMPRAR LAS MEJORES ACCIONES EN FUNCION DE SU PORCENTAJE DE ACIERTO (50%) ] /////////////////////////
	
	public void setPosicion(int ejecucion, int posicion) {
		
		main_TDR_Leo.set_posicion_estudiante(ejecucion, posicion);
		
	}
	
}

class gato extends inversor{ //INVERSOR GATO
	
	private archivo log;
	
	public gato (String id, archivo archivo) {
				
		super(id, archivo);
		
		log=archivo;
		
		log.escribir("[" + id + "] - Probabilidad de acierto: ALEATORIO");

	}

	public void comprar_todas_acciones(empresa[] empresas) {
				
		double dinero_aleatorio=super.getDinero();
				
		log.escribir("Inversor [" + super.getID() + "] - Comprando acciones aleatorias con " + dinero_aleatorio + " rupias de " + super.getDinero() + " rupias en total.");
		
		ArrayList<empresa> posibles_acciones;
		
		// [COMPRANDO TODAS LAS ACCIONES QUE PUEDA] /////////////////////////////////////////////////
		
		do {
			
			posibles_acciones=new ArrayList<empresa>();
							
			for(empresa acciones: empresas) { //BUSCA TODAS LAS ACCIONES QUE PUEDE COMPRAR
				
				if(dinero_aleatorio>=acciones.getValor_acciones()) {
					
					posibles_acciones.add(acciones);
					
				}
				
			}
			
			log.escribir("- Inversor [" + super.getID() + "] - A " + posibles_acciones.size() + " empresas que puede comprar acciones...");

			int aleatorio=(int) (Math.random()*100);

			if(posibles_acciones.size()>0) {
							
				double dividendo=(double)100/posibles_acciones.size();
								
				int n_accion=(int)(aleatorio/dividendo); //ELIJE UNA POSICION ALEATORIA DE 'posibles_acciones'
												
				dinero_aleatorio-=posibles_acciones.get(n_accion).getValor_acciones();
				
				super.comprar_accion(posibles_acciones.get(n_accion));
									
			}
											
			}while(posibles_acciones.size()>0);
		
			log.escribir("- Inversor [" + super.getID() + "] - Ya ha comprado todas las acciones que podia...");
		
		// [COMPRANDO TODAS LAS ACCIONES QUE PUEDA] /////////////////////////////////////////////////
			
	}	
		
	public void setPosicion(int ejecucion, int posicion) {
		
		main_TDR_Leo.set_posicion_gato(ejecucion, posicion);
		
	}
	
}
*/

class mensaje{
	
	private String text, text2, title;
	private int i;
	
	public mensaje(String _text, String _text2, String _title, int id) {
		text=_text;
		text2=_text2;
		title=_title;
		i=id;
	}
	
	public int show() {
		
		JPanel mensaje=new JPanel();
		Box v=Box.createVerticalBox();
		
		JLabel l1=new JLabel(text);
		JLabel l2=new JLabel(text2);
		
		l1.setFont(new Font("Arial", Font.PLAIN, 13));
		l2.setFont(new Font("Arial", Font.PLAIN, 13));
		
		JCheckBox c=new JCheckBox("No volver a mostrar");
		c.setFont(new Font("Verdana", Font.PLAIN, 11));
		
		c.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		v.add(l1);
		v.add(l2);
		v.add(c);
		mensaje.add(v);
		
		if(config.getMostrar_Warnings(i)){
			
			int aceptar=JOptionPane.showConfirmDialog(null, mensaje, title, JOptionPane.YES_NO_OPTION);
			
			config.setMostrar_Warnings(i, !c.isSelected());
			
			return aceptar;
			
		}else {
			return 0;
		}
		
	}
	
}

class archivo{ //CLASE QUE SE ENCARGA REGISTRAR TODOS LOS PROCESOS EN UN ARCHIVO DE VOLCADO (file=Bolsa de valores/logs/log.txt) 
	
	private File archivo;
	private FileOutputStream log;
	private BufferedWriter buffer;
	
	public archivo(int num) {
		
		archivo=new File("Bolsa de valores/logs/log" + num + ".txt");
		
		try {
			
			archivo.createNewFile();
			
			log=new FileOutputStream(archivo);
			
			buffer=new BufferedWriter(new OutputStreamWriter(log));
			
		} catch (IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().toString(), 0);
			e.printStackTrace();
		}
	}
	
	public void escribir(String texto) {
		
		try {
			
			System.out.println(texto);
			
			buffer.write(texto);
			
			buffer.newLine();
						
		} catch (IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().toString(), 0);
			e.printStackTrace();
		}
		
	}
	
	public void salto() {
		
		try {
			
			System.out.println("");
			
			buffer.newLine();
						
		} catch (IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().toString(), 0);		
			e.printStackTrace();
		}
		
	}
	
	public void close() {
		
		try {
			buffer.close();
		} catch (IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().toString(), 0);		
			e.printStackTrace();	
		}
		
	}
	
}

class archivo_resultado{ //CLASE QUE SE ENCARGA DE REGISTRAR TODOS LOS RESULTADOS DE LAS EJECUCIONES (file=Bolsa de valores/resultados_log.txt) 
	
	private File archivo;
	private FileOutputStream log;
	private BufferedWriter buffer;
	private ArrayList<Thread> threads;
	private int id_thread;
	private Lock cierre=new ReentrantLock();
	private Condition orden=cierre.newCondition();
	private int[][] inversores;
	private ArrayList<inversor[]> resumen;
	
	public archivo_resultado() {
		
		archivo=new File("Bolsa de valores/resultados_log.txt");
		
		try {
			
			archivo.createNewFile();
			
			log=new FileOutputStream(archivo);
			
			buffer=new BufferedWriter(new OutputStreamWriter(log));
			
			threads=new ArrayList<Thread>();
			
			resumen=new ArrayList<inversor[]>();
			
			id_thread=0;
			
		} catch (IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().toString(), 0);
			e.printStackTrace();
		}
	}
	
	public void new_thread(Thread t) {
		
		threads.add(t);
		
	}
	
	public void escribir(int num_ejecucion, inversor [] inversores) {
			
		cierre.lock();
		
		try {
			
			while(Thread.currentThread()!=threads.get(id_thread)) {
				
				try {
					orden.await();
				} catch (InterruptedException e) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().toString(), 0);
					e.printStackTrace();
				}
				
			}
			
			resumen.add(inversores);
			
			buffer.write("--------- RESULTADOS (" + num_ejecucion + ") ---------");
			
			buffer.newLine();
			
			for(inversor i: inversores) {
				
				buffer.newLine();
				
				buffer.write("Inversor [" + i.getID() + "] - Dinero: " + i.getDinero() + " rupias");
				
				
				
			}
			
			buffer.newLine();
			buffer.newLine();
						
			id_thread++;
			orden.signalAll();
			
		} catch (IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().toString(), 0);
			e.printStackTrace();
			
		}finally {
			
			cierre.unlock();
			
		}
		
		
	}
		
	public void posiciones(int num_inversores, String[] nombre_inversores, int[][] posiciones) { //CREA LA TABLA FINAL DEL RESUMEN DE LAS POSICIONES DE LOS INVERSORES
						
		inversores=new int[num_inversores][num_inversores];
		
		for(int i=0; i<inversores.length; i++) {
			
			for(int j=0; j<inversores[i].length; j++) {
			
				inversores[i][j]=0;
				
			}			
		}
		
		for(int i=0; i<posiciones.length; i++) {
			
			for(int j=0; j<posiciones[i].length; j++) {
				
				if(posiciones[i][j]!=-1) {
				
					inversores[posiciones[i][j]][j]++;
					
				}				
			}
			
		}
		
		try {
			
			
			buffer.write("+------------+");
			
			for(int i=0; i<num_inversores; i++) {
				
				buffer.write("------+");
				
			}
			
			buffer.newLine();
			
			buffer.write("|	     |");
			
			for(int i=0; i<num_inversores; i++) {
				
				buffer.write("  " + (i+1) + "º  |");
				
			}
			
			//buffer.write("|	     |  1º  |  2º  |  3º  |"); buffer.newLine();
			buffer.newLine();
			buffer.write("+------------+");
			
			for(int i=0; i<num_inversores; i++) {
				
				buffer.write("------+");
				
			}
			
			buffer.newLine();
			
			for(int i=0; i<inversores.length; i++) {
				
				buffer.write("|" + nombre_inversores[i] + ":|");
				
				for(int j=0; j<inversores[i].length; j++) {
					
					buffer.write("  " + inversores[i][j] + "  |");
					
				}
				
				buffer.newLine();
				
				//buffer.write("|" + nombre_inversores[i] + ":| " + numPrimero[i] + " | " + numSegundo[i] + " | " + numTercero[i] + " |"); buffer.newLine();
				
				buffer.write("+------------+"); 
				
				for(int j=0; j<num_inversores; j++) {
					
					buffer.write("------+");
					
				}
				
				buffer.newLine();

			}
						
			/*buffer.write("|PROFESIONAL:|  " + pro[0] + " |  " + pro[1] + " |  " + pro[2] + " |"); buffer.newLine();
			buffer.write("+------------+------+------+------+"); buffer.newLine();
			buffer.write("|ESTUDIANTE: |  " + estudiante[0] + " |  " + estudiante[1] + " |  " + estudiante[2] + " |"); buffer.newLine();
			buffer.write("+------------+------+------+------+"); buffer.newLine();
			buffer.write("|GATO:       |  " + gato[0] + " |  " + gato[1] + " |  " + gato[2] + " |"); buffer.newLine();*/

			
		} catch (IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().toString(), 0);
			e.printStackTrace();
		}
		
	}
	
	public void firma() { //FIRMA
		
		try {
			
			buffer.newLine();
			buffer.newLine();
			buffer.newLine();
			buffer.newLine();
			buffer.newLine();
			buffer.newLine();

			buffer.write("https://github.com/daniieelgs/bolsa_valores");
			buffer.newLine();
			buffer.write("daniieelgs@gmail.com");
			buffer.newLine();
			buffer.write("Copyright (c) 2021 DANIEL GARCIA SERRANO");
			
		} catch (IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().toString(), 0);
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<inversor[]> getResumen(){
		
		return resumen;
		
	}
	
	public int[][] getTabla_Posiciones(){
		
		return inversores;
		
	}
	
	public void close() {
		
		try {
			buffer.close();
		} catch (IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().toString(), 0);
			e.printStackTrace();
		}
		
	}
	
}
