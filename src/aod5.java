import java.applet.*;
import java.awt.*;
import java.awt.Dimension;
import java.awt.image.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.lang.Math;
import java.lang.Object;
import java.awt.Graphics;
import java.awt.image.PixelGrabber;
import java.awt.Event;
import java.io.*;

//public class aod extends java.applet.Applet implements Runnable {
public class aod5 extends java.applet.Applet {


URL url;
Image cinza,imagem;
//Thread runner;
int cx,cy,zero_x,zero_y,r,r2,largura,altura,n_linhas,n_vistas;
int[] pixels,pixels2; // o array da imagem e do resultado
float[][] pontos; // o resultado das integrais de cada ponto
Font fonte=new Font("TimesRoman",Font.BOLD,14);
Integer it;
public Button start_button;
TextField entrada_linhas,entrada_vistas;
Choice menu;

float dx,angulo,da;

//public void start () {
//    if (runner == null ) { runner = new Thread(this);  runner.start(); }
//}

//public void stop() {
//    if (runner != null ) { runner.stop(); runner=null; }
//}

public void le_imagem_url(String nome_imagem) {
//    try { url = new URL("http://www.uninet.com.br/lituni.gif"); }
//    catch ( MalformedURLException e) {
//      showStatus(" URL " + url + " errado");
//   }
//    imagem = getImage(url);

//    try { imagem = (Image)url.getContent(); }
//    catch ( IOException e) {
//      showStatus(" formato de imagem nao reconhecido");
//    }


//    MediaTracker tracker=new MediaTracker(this);
//    tracker.addImage(imagem,0);
//    try { tracker.waitForAll(); }
//    catch (InterruptedException e) {
//      showStatus("Error esperando imagem do URL.");
//    }
}

public void le_imagem_disco(String nome_imagem) {
    PixelGrabber pg;

    imagem = getImage(this.getCodeBase(), nome_imagem);

     altura=0;

     while (altura<1) {
        //----- getHeight e getWidth sao assincronos
        altura=imagem.getHeight(this); largura=imagem.getWidth(this);
     }


    pixels=new int[largura*altura+10];

// o bloco abaixo e so para corrigir o dithering do netscape
  pg = new PixelGrabber(imagem,0,0,largura,altura,pixels,0,largura);
   try { pg.grabPixels(); }
   catch (InterruptedException e) { return; }
    imagem=createImage(new MemoryImageSource(largura,altura,pixels,0,largura));

    ImageFilter f = new GrayFilter();
    ImageProducer producer = new FilteredImageSource(imagem.getSource(),f);
    cinza = this.createImage(producer);

    pg = new PixelGrabber(cinza,0,0,largura,altura,pixels,0,largura);
    try { pg.grabPixels(); }
    catch (InterruptedException e) { return; }

    r=(int)(Math.sqrt(altura/2*altura/2+largura/2*largura/2 ) +1 );
    r2=(int)(r*Math.sqrt(2));
    zero_x=cx-largura/2; zero_y=cy-altura/2;
    n_linhas=(int)r/2;
    n_vistas=(int)Math.min(n_linhas/2,200);

    pontos=new float[largura+10][altura+10];

}

public float qy(float y) {
    int MAX=(int)(n_linhas/2),i;
    double d,integral,x,cos;

    d=(dx/2)/MAX;
    x=0; integral=0;


    for (i=0;i<=MAX;i++) {
        cos=Math.cos(2*Math.PI*x*y);
        integral+=x*(.54+.46*cos)*cos;
        x+=d;
    }

    return (float) (2*integral*d);
}

public float rxy(int i, int j) {
    int x=i-(int)(largura/2); int y=j-(int)(altura/2);
    return (float)Math.sqrt(x*x+y*y);
}

public float teta(int i,int j) {
   float angulo,raio;

   raio=rxy(i,j);

   float x=i-(int)(largura/2); float y=j-(int)(altura/2);

   if (raio==0) { return 0; }

   angulo=(float)Math.acos(x/raio);

   if (y<0) { angulo=(float)(2*Math.PI-angulo); }

   return angulo;
}


public void init() {

   Dimension d; d=this.size();

   cx = d.width/2; cy=d.height/2;

   le_imagem_disco("brain.jpg");

   monta_painel();

		//{{INIT_CONTROLS
		setLayout(null);
		addNotify();
		resize(430,270);
		//}}
}

public void monta_painel() {
    menu=new Choice();
    menu.addItem("brain.jpg");
    menu.addItem("bacia.jpg");
    menu.addItem("barriga.jpg");
    this.add(menu);
    this.add( new Label("numero de linhas"));
    entrada_linhas=new TextField(3);
    entrada_linhas.setText(it.toString(n_linhas));
    this.add(entrada_linhas);
    this.add( new Label("numero de vistas"));
    entrada_vistas=new TextField(3);
    entrada_vistas.setText(it.toString(n_vistas));
    this.add(entrada_vistas);
    start_button=new Button("Comecar CT");
    this.add(start_button);
//    this.add(new Label(nome_imagem+"  "+it.toString(largura)+"x"+it.toString(altura),Label.LEFT));
}


//public void run() {
//
//}

public boolean handleEvent (Event evento) {
   switch(evento.id) {

       case evento.ACTION_EVENT: {
          if (evento.target==start_button) {
               le_imagem_disco(menu.getSelectedItem());
               imagem=cinza;
               n_linhas=it.parseInt(entrada_linhas.getText());
               if (n_linhas>r) n_linhas=r;
               if (n_linhas<1) n_linhas=1;
               n_vistas=it.parseInt(entrada_vistas.getText());
               if (n_vistas>200) n_vistas=200;
               if(n_vistas<1) n_vistas=1;
               start_scan();
               showStatus("Scanning");
               desenha_imagem_integrada();
               return true;
          }
       }
   }
   return false;

}



public float calcula_wij(double x1,double y1,double x2,double y2,int i,int j) {
   int n_pontos=0;

   double ix[]=new double [3];
   double iy[]=new double [3];
   double x,y,a,b;

   if (y1==y2) {
      a=Math.max(Math.min(x1,x2),(i-.5));
      b=Math.min(Math.max(x1,x2),(i+.5));
      return (float)Math.max((b-a),0);
   }

   if (x1==x2) {
      a=Math.max(Math.min(y1,y2),(j-.5));
      b=Math.min(Math.max(y1,y2),(j+.5));
      return (float)Math.max((b-a),0);
   }

  a=(y2-y1)/(x2-x1);

  if (n_pontos<2) {
    x=( (j-.5)-y1 )/a+x1;
    if ( ((i-.5)<=x)&&(x<=(i+.5)) ) {
        n_pontos++;
        ix[n_pontos]=x; iy[n_pontos]=j-.5;
    }
  }

  if (n_pontos<2) {
    x=( (j+.5)-y1 )/a+x1;
    if ( ((i-.5)<=x)&&(x<=(i+.5)) ) {
        n_pontos++;
        ix[n_pontos]=x; iy[n_pontos]=j+.5;
    }
  }

  if (n_pontos<2) {
    y=a*((i-.5)-x1)+y1;
    if ( ((j-.5)<=y)&&(y<=(j+.5)) ) {
        n_pontos++;
        ix[n_pontos]=i-.5; iy[n_pontos]=y;
    }
  }

  if (n_pontos<2) {
    y=a*((i+.5)-x1)+y1;
    if ( ((j-.5)<=y)&&(y<=(j+.5)) ) {
        n_pontos++;
        ix[n_pontos]=i+.5; iy[n_pontos]=y;
    }
  }

      return (float)Math.sqrt( (ix[2]-ix[1])*(ix[2]-ix[1])+(iy[2]-iy[1])*(iy[2]-iy[1]) );
}


public void start_scan() {
   int i,j;

   Graphics g = this.getGraphics();

   da=(float)Math.PI/n_vistas;
   angulo=0;

   for(i=0;i<largura;i++) for (j=0;j<altura;j++) pontos[i][j]=0;

//   try { Thread.sleep(3000); }
//   catch (InterruptedException e) {}


  while(angulo<=Math.PI) {

    desenho_fixo(g);

    calcula_vista(g,angulo);

    angulo=angulo+da;
  }

}

public void paint (Graphics g) {
    desenho_fixo(g);
}


public void desenho_fixo (Graphics g) {
    g.setFont(fonte);
    g.setColor(Color.white);
//    g.fillRect(cx-r2-20,cy-r2-20,2*r2+40,2*r2+40);
    g.fillRect(0,0,2*cx,2*cy);
    g.setColor(Color.black);
    g.fillOval(cx-r2,cy-r2,2*r2,2*r2);
    g.setColor(Color.red);
    g.drawOval(cx-r2,cy-r2,2*r2,2*r2);
    g.drawOval(cx-r,cy-r,2*r,2*r);
    g.drawImage(imagem,zero_x,zero_y,this);
}


public void calcula_vista (Graphics g, float angulo) {
   float rpx0,rpx1,rpy0,rpy1,rx0,rx1,rx2,rx3,ry0,ry1,ry2,ry3;
   float px0,px1,py0,py1;
   int x0,x1,x2,x3,y0,y1,y2,y3;
   float coss,sinn;

   int n1,n2,i,j;
   float t,x,y,p1,p2;

   float soma=0;

   dx=(float)2*r/n_linhas;

   x0=-r; y0=-r; x1=r; y1=-r;
   x2=-r; y2=r; x3=r; y3=r;

      coss=(float)Math.cos(angulo);
      sinn=(float)Math.sin(angulo);

      rx0=x0*coss-y0*sinn; ry0=x0*sinn+y0*coss;
      rx1=x1*coss-y1*sinn; ry1=x1*sinn+y1*coss;
      rx2=x2*coss-y2*sinn; ry2=x2*sinn+y2*coss;
      rx3=x3*coss-y3*sinn; ry3=x3*sinn+y3*coss;

      g.setColor(Color.white);
      g.drawLine((int)Math.round(cx+rx0),(int)Math.round(cy+ry0),(int)Math.round(cx+rx1),(int)Math.round(cy+ry1));
      g.drawLine((int)Math.round(cx+rx2),(int)Math.round(cy+ry2),(int)Math.round(cx+rx3),(int)Math.round(cy+ry3));

      float [] p=new float[n_linhas+1];

      for (i=1;i<=n_linhas;i++) {
         px0=(x0+i*dx)*coss-y0*sinn; py0=(x0+i*dx)*sinn+y0*coss;
         px1=(x2+i*dx)*coss-y2*sinn; py1=(x2+i*dx)*sinn+y2*coss;

         g.setColor(Color.white);
         g.drawLine((int)Math.round(cx+px0),(int)Math.round(cy+py0),(int)Math.round(cx+px1),(int)Math.round(cy+py1));

         p[i-1]=line2((int)Math.round(cx+px0),(int)Math.round(cy+py0),(int)Math.round(cx+px1),(int)Math.round(cy+py1),Color.white);

         g.setColor(Color.black);
         g.drawLine((int)Math.round(cx+px0),(int)Math.round(cy+py0),(int)Math.round(cx+px1),(int)Math.round(cy+py1));
      }

      float [] pc=new float[n_linhas+1];

      for(i=0;i<n_linhas;i++) {
        for (j=0;j<n_linhas;j++) {
           pc[i]+=p[j]*qy( ((i-n_linhas/2)-(j-n_linhas/2))*dx );
        }
        pc[i]=pc[i]*dx;
      }


      for (i=0;i<largura;i++) {
         for (j=0;j<altura;j++) {
            x=(i-largura/2); y=(j-altura/2);
            Math.atan2(x,y);
            x=x*(float)Math.cos(angulo-y);
//            x=rxy(i,j)*(float)Math.cos(angulo-teta(i,j));
            n1=(int)(x/dx+n_linhas/2);

            n2=n1+1;

            t=(x-(n1-n_linhas/2)*dx)/dx;

            pontos[i][j]+=da*(t*pc[n1]+(1-t)*pc[n2]);
         }
      }

      g.drawLine((int)Math.round(cx+rx0),(int)Math.round(cy+ry0),(int)Math.round(cx+rx1),(int)Math.round(cy+ry1));
      g.drawLine((int)Math.round(cx+rx2),(int)Math.round(cy+ry2),(int)Math.round(cx+rx3),(int)Math.round(cy+ry3));




}



public int sgn(float x) {
int sinal=0;
   if (x>0) { sinal=1;}
   if (x<0) { sinal=-1;}
return sinal;
}

public float line2(int x1, int y1, int x2,int y2, Color col) {

// Graphics g=this.getGraphics();

   int u,v,d1x,d1y,d2x,d2y,m,n;
   int i,j,x,y,ix,iy;
   int s;

   float integral,wij;
   int n_pontos,n_pontos_dentro;

//   g.setColor(Color.red);

   u=x2-x1; v=y2-y1;
   d1x=sgn(u); d1y=sgn(v); d2x=sgn(u); d2y=0;
   m=Math.abs(u); n=Math.abs(v);
   if (m<=n) {
      d2x=0; d2y=sgn(v); m=Math.abs(v); n=Math.abs(u);
   }

   x=x1; y=y1;
   s=m/2; n_pontos=m+1;

   integral=0; n_pontos_dentro=0;
   for (i=0;i<n_pontos;i++) {

      ix=x-(cx-(int)largura/2); iy=y-(cy-(int)altura/2);
      if (((0<=ix)&&(ix<largura))&&((0<=iy)&&(iy<altura))) {
         wij=calcula_wij(x1,y1,x2,y2,x,y);
         integral+=(wij*(pixels[iy*largura+ix]&0x000000ff));
         n_pontos_dentro++;
      }

  //  g.drawLine(x,y,x,y);
      s=s+n;
      if (s>=m) {
         s=s-m;
         x=x+d1x;
         y=y+d1y;
      } else {
         x=x+d2x;
         y=y+d2y;
      }
   }

 return (float)integral;

}

public float filtro (float x) {
   float y=0;
//   if (x<=10) { y=(1/3)*x; }
//   if (x>3) { y=0; }

   return x;
}

public void desenha_imagem_integrada() {
   float x,max=1,min=0;
   int i,j,c;

   pixels2=new int[largura*altura];

   Graphics g=this.getGraphics();

   for(i=5;i<largura-5;i++) for (j=5;j<altura-5;j++) {
      if (pontos[i][j]<min) { min=pontos[i][j]; }
   }

   for(i=5;i<largura-5;i++) for (j=5;j<altura-5;j++) {
      pontos[i][j]=(float)((pontos[i][j]-min)+0.001);
      if (pontos[i][j]>max) { max=pontos[i][j]; }
   }

   for(i=0;i<largura;i++) for (j=0;j<altura;j++) {
      x=pontos[i][j]/max;
      c=(int)Math.round(255*filtro(x));
      pixels2[j*largura+i]=0xff000000|c<<16|c<<8|c;
   }
   imagem=createImage(new MemoryImageSource(largura,altura,pixels2,0,largura));
   g.drawImage(imagem,zero_x,zero_y,this);
}






	//{{DECLARE_CONTROLS
	//}}
} // do applet



class GrayFilter extends RGBImageFilter {
   public GrayFilter() { canFilterIndexColorModel = true; }
   public int filterRGB(int x, int y,int rgb) {
      int a = rgb & 0xff000000;
      int r = (rgb & 0x00ff0000)>>16;
      int g = (rgb & 0x0000ff00)>>8;
      int b = (rgb & 0x000000ff);
      int avg = (r+g+b)/3;
      return a | avg<<16 | avg<<8 | avg ; // or
   }
}

