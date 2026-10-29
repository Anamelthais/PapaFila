package com.papafila.ui;

import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.DateFormat;
import java.util.Date;
import android.os.Handler;
import android.graphics.Color;
import android.widget.ImageView;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.papafila.data.CatalogRepository;
import com.papafila.data.DemoCatalogRepository;
import com.papafila.model.Product;
import com.papafila.service.Cart;
import com.papafila.service.Order;
import com.papafila.service.OrderService;
import com.papafila.service.OrderTracker;
import java.util.List;
import java.util.Locale;

/** Camada de apresentação: regras de negócio ficam nos serviços. */
public final class MainActivity extends Activity {
    private static final int ORANGE=Color.rgb(255,91,12), CREAM=Color.rgb(255,249,239), DARK=Color.rgb(42,32,26);
    private final CatalogRepository catalog = new DemoCatalogRepository();
    private final Cart cart = new Cart();
    private final OrderService orderService = new OrderService();
    private final OrderTracker tracker = new OrderTracker();
    private final Handler handler = new Handler();
    private final List<Product> products = catalog.list();
    private LinearLayout root, content;
    private Order currentOrder;
    private TextView countdownView;
    private String userName="";
    private final java.util.ArrayList<String> orderItems = new java.util.ArrayList<>();
    private String screen="home", category="Todos";
    private final Runnable tick = new Runnable() { @Override public void run() {
        if(currentOrder!=null && screen.equals("tracking")) { tracker.refresh(currentOrder,System.currentTimeMillis()); if(currentOrder.status()==Order.Status.PRONTO) {saveOrderStatus();navigate("ready");} else updateCountdown(); }
        handler.postDelayed(this,1000);
    }};
    @Override public void onCreate(Bundle b) { super.onCreate(b); getWindow().setStatusBarColor(ORANGE); getWindow().setNavigationBarColor(DARK); userName=getPreferences(MODE_PRIVATE).getString("user", ""); render(); }
    @Override protected void onPause() { handler.removeCallbacks(tick); super.onPause(); }
    @Override protected void onResume() { super.onResume(); handler.removeCallbacks(tick); handler.postDelayed(tick,1000); }
    @Override protected void onDestroy() { handler.removeCallbacks(tick); super.onDestroy(); }
    private int dp(int x) { return (int)(getResources().getDisplayMetrics().density*x+0.5f); }
    private GradientDrawable bg(int color,int radius) { GradientDrawable g=new GradientDrawable(); g.setColor(color); g.setCornerRadius(dp(radius)); return g; }
    private TextView text(String value,int size,boolean bold) {
        TextView t=new TextView(this); t.setText(value); t.setTextColor(DARK); t.setTextSize(size); t.setTypeface(Typeface.create("sans-serif-rounded",bold?Typeface.BOLD:Typeface.NORMAL)); t.setPadding(0,dp(5),0,dp(5)); return t;
    }
    private void heading(String value) { content.addView(text(value,25,true)); }
    private void label(String value) { content.addView(text(value,15,false)); }
    private void space(int px) { View v=new View(this); content.addView(v,new LinearLayout.LayoutParams(1,dp(px))); }
    private LinearLayout vertical() { LinearLayout l=new LinearLayout(this); l.setOrientation(LinearLayout.VERTICAL); return l; }
    private LinearLayout horizontal() { LinearLayout l=new LinearLayout(this); l.setOrientation(LinearLayout.HORIZONTAL); l.setGravity(Gravity.CENTER_VERTICAL); return l; }
    private void card(LinearLayout item) { item.setPadding(dp(15),dp(13),dp(15),dp(13)); item.setBackground(bg(Color.WHITE,20)); LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2); p.setMargins(0,dp(6),0,dp(6)); content.addView(item,p); }
    private Button button(String value,Runnable action) { Button b=new Button(this); b.setAllCaps(false); b.setText(value); b.setTextSize(15); b.setTypeface(Typeface.create("sans-serif-rounded",Typeface.BOLD)); b.setTextColor(Color.WHITE); b.setBackground(bg(ORANGE,18)); b.setPadding(dp(12),dp(8),dp(12),dp(8)); b.setOnClickListener(v->action.run()); return b; }
    private Button paleButton(String value,Runnable action) { Button b=button(value,action); b.setTextColor(DARK); b.setBackground(bg(Color.rgb(255,232,206),18)); return b; }
    private void fullButton(String value,Runnable action) { LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(54)); p.setMargins(0,dp(9),0,dp(9)); content.addView(button(value,action),p); }
    private void navigate(String s) { screen=s; countdownView=null; render(); }
    private SharedPreferences prefs(){return getPreferences(MODE_PRIVATE);}
    private JSONArray history(){try{return new JSONArray(prefs().getString("history","[]"));}catch(Exception e){return new JSONArray();}}
    private void saveOrderStatus(){if(currentOrder==null)return;try{JSONArray old=history(), updated=new JSONArray();for(int i=0;i<old.length();i++){JSONObject o=old.getJSONObject(i);if(currentOrder.code.equals(o.optString("code")))o.put("status",currentOrder.status().name());updated.put(o);}prefs().edit().putString("history",updated.toString()).apply();}catch(Exception ignored){}}
    private void recordOrder(){try{JSONArray a=history();JSONObject o=new JSONObject();o.put("code",currentOrder.code);o.put("total",currentOrder.totalCents);o.put("date",currentOrder.placedAtMillis);o.put("status",currentOrder.status().name());o.put("items",android.text.TextUtils.join(", ",orderItems));a.put(o);prefs().edit().putString("history",a.toString()).apply();}catch(Exception ignored){}}
    private void updateCountdown(){if(currentOrder==null||countdownView==null)return;long ms=tracker.remainingMillis(currentOrder,System.currentTimeMillis());long sec=(ms+999)/1000;countdownView.setText("⏱ "+String.format(Locale.US,"%02d:%02d",sec/60,sec%60));}
    private String money(int cents) { return String.format(new Locale("pt","BR"),"R$ %.2f",cents/100.0); }
    private void render() {
        root=vertical(); root.setBackgroundColor(CREAM); setContentView(root);
        LinearLayout bar=horizontal(); bar.setPadding(dp(20),dp(12),dp(20),dp(12)); bar.setBackgroundColor(ORANGE);
        ImageView mascot=new ImageView(this); mascot.setImageResource(com.papafila.R.drawable.monster); bar.addView(mascot,new LinearLayout.LayoutParams(dp(44),dp(44))); TextView logo=text(" PapaFila",25,true); logo.setTextColor(Color.WHITE); bar.addView(logo,new LinearLayout.LayoutParams(0,-2,1));
        TextView cartIcon=text("🛒 "+cart.count()+" · "+money(cart.total(products)),14,true); cartIcon.setTextColor(Color.WHITE); cartIcon.setOnClickListener(v->navigate("cart")); bar.addView(cartIcon); root.addView(bar);
        ScrollView scroll=new ScrollView(this); scroll.setFillViewport(true); content=vertical(); content.setPadding(dp(20),dp(16),dp(20),dp(22)); scroll.addView(content); root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        switch(screen) { case "cart": showCart(); break; case "checkout": showCheckout(); break; case "tracking": showTracking(); break; case "ready": showReady(); break; case "login":showLogin();break; case "profile":showProfile();break;case "history":showHistory();break; default: showHome(); }
        LinearLayout nav=horizontal(); nav.setBackgroundColor(Color.WHITE); nav.setPadding(dp(8),dp(4),dp(8),dp(4));
        Button home=paleButton("⌂ Início",()->navigate("home")); nav.addView(home,new LinearLayout.LayoutParams(0,dp(48),1));
        Button basket=paleButton("🛒 Carrinho",()->navigate("cart")); nav.addView(basket,new LinearLayout.LayoutParams(0,dp(48),1));
        Button orders=paleButton("◷ Pedidos",()->navigate("history")); nav.addView(orders,new LinearLayout.LayoutParams(0,dp(48),1)); Button profile=paleButton("♙ Perfil",()->navigate(userName.isEmpty()?"login":"profile"));nav.addView(profile,new LinearLayout.LayoutParams(0,dp(48),1));root.addView(nav);
    }
    private void showHome() {
        heading("Bateu a fome? 😋"); label("Cantina CESAR • Seu lanche, sem fila."); if(!userName.isEmpty())label("Olá, "+userName+"!"); space(8);
        LinearLayout hero=vertical(); hero.setPadding(dp(18),dp(16),dp(18),dp(16)); hero.setBackground(bg(Color.rgb(255,224,190),22));
        hero.addView(text("O monstrinho tá com fome!",21,true)); hero.addView(text("Peça agora e retire sem esperar na fila.",15,false)); content.addView(hero); space(12);
        LinearLayout daily=vertical();daily.setPadding(dp(18),dp(15),dp(18),dp(15));daily.setBackground(bg(Color.rgb(255,236,205),22));
        daily.addView(text("⭐ SUGESTÃO DO DIA",13,true));daily.addView(text("👾 Combo Fome Monstruosa",21,true));daily.addView(text("Alimente seu monstrinho interior!",15,false));daily.addView(text("Coxinha + suco de laranja • "+money(1000),17,true));
        daily.addView(button("Adicionar combo ao carrinho +",()->{for(Product p:products)if(p.id.equals("com")){cart.add(p);break;}render();}));content.addView(daily);space(12);
        if(cart.count()>0){LinearLayout total=vertical();total.addView(text("🛒 Sua seleção: "+cart.count()+" itens • "+money(cart.total(products)),18,true));card(total);}
        LinearLayout filters=horizontal(); for(String c:new String[]{"Todos","Salgados","Bebidas","Combos"}) { Button b=paleButton(c,()->{category=c; render();}); if(category.equals(c)) {b.setBackground(bg(ORANGE,18)); b.setTextColor(Color.WHITE);} LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,dp(46),1); p.setMargins(dp(2),0,dp(2),0); filters.addView(b,p); } content.addView(filters); space(12);
        heading("Cardápio do dia");
        for(Product product:products) { if(!category.equals("Todos")&&!product.category.equals(category)) continue;
            LinearLayout row=horizontal(); TextView emoji=text(product.emoji,34,false); row.addView(emoji); LinearLayout info=vertical(); info.setPadding(dp(12),0,dp(8),0); info.addView(text(product.name,18,true)); info.addView(text(product.description,12,false)); info.addView(text(money(product.priceCents),16,true)); row.addView(info,new LinearLayout.LayoutParams(0,-2,1)); row.addView(button("+",()->{cart.add(product); render(); Toast.makeText(this,"Adicionado ao carrinho!",Toast.LENGTH_SHORT).show();}),new LinearLayout.LayoutParams(dp(54),dp(52))); card(row);
        }
        if(cart.count()>0) fullButton("Ver carrinho • "+cart.count()+" itens • "+money(cart.total(products)),()->navigate("cart"));
    }
    private void showCart() {
        heading("Seu carrinho 🛒"); if(cart.count()==0) {label("Seu carrinho está vazio. Escolha um salgado!"); fullButton("Explorar cardápio",()->navigate("home")); return;}
        for(Product product:products) {int q=cart.quantity(product); if(q==0) continue;
            LinearLayout row=horizontal(); LinearLayout info=vertical(); info.addView(text(product.emoji+" "+product.name,17,true)); info.addView(text(money(product.priceCents*q),14,false)); row.addView(info,new LinearLayout.LayoutParams(0,-2,1));
            row.addView(paleButton("−",()->{cart.remove(product);render();})); row.addView(text("  "+q+"  ",17,true)); row.addView(button("+",()->{cart.add(product);render();})); card(row);
        }
        space(15); heading("Total: "+money(cart.total(products))); label("Pagamento demonstrativo. Nenhuma cobrança será realizada."); fullButton("Confirmar compra",()->navigate("checkout"));
    }
    private void showCheckout() {
        heading("Finalizar pedido"); label("Revise seu pedido antes de confirmar."); space(10); heading("Total: "+money(cart.total(products)));
        label("Forma de pagamento: simulado (sem cobrança)"); label("Tempo estimado: 10 minutos");
        fullButton("Confirmar pedido",()->{try {orderItems.clear();for(Product p:products){int q=cart.quantity(p);if(q>0)orderItems.add(q+"x "+p.name);}currentOrder=orderService.confirm(cart.total(products));recordOrder();cart.clear();navigate("tracking");} catch(IllegalArgumentException e){Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();navigate("cart");}});
        content.addView(paleButton("Voltar ao carrinho",()->navigate("cart")));
    }
    private void showTracking() {
        if(currentOrder==null) {navigate("home");return;} tracker.refresh(currentOrder,System.currentTimeMillis()); if(currentOrder.status()==Order.Status.PRONTO) {navigate("ready");return;}
        heading("Pedido confirmado! ✅"); label("Seu pedido está em preparo."); space(12);
        LinearLayout info=vertical(); info.addView(text("Código de confirmação",15,false)); info.addView(text(currentOrder.code,34,true)); info.addView(text("Total: "+money(currentOrder.totalCents),16,false)); card(info);
        space(12); countdownView=text("",25,true);content.addView(countdownView);updateCountdown(); label("Seu pedido estará pronto em 10 minutos.");
        label("✓ Pedido confirmado"); label("🍳 Em preparo"); label("○ Pronto para retirada"); space(12);
        label("Para a demonstração, o cronômetro simula o preparo.");
        fullButton("Simular pedido pronto",()->{currentOrder.setStatus(Order.Status.PRONTO);saveOrderStatus();navigate("ready");});
    }
    private void showReady() {
        if(currentOrder==null) {navigate("home");return;}
        heading("🎉 Pedido pronto para retirada!"); label("O monstrinho aprovou! Vá ao balcão da cantina."); space(15);
        LinearLayout info=vertical(); info.addView(text("Apresente este código",17,false)); info.addView(text(currentOrder.code,40,true)); info.addView(text("Pedido: "+money(currentOrder.totalCents),15,false)); card(info);
        if(currentOrder.status()!=Order.Status.RETIRADO) fullButton("Confirmar retirada",()->{currentOrder.setStatus(Order.Status.RETIRADO);saveOrderStatus();Toast.makeText(this,"Pedido retirado! Bom apetite 😋",Toast.LENGTH_LONG).show();navigate("home");});
        else label("✓ Pedido já retirado. Bom apetite!");
        content.addView(paleButton("Voltar ao início",()->navigate("home")));
    }
    private void showLogin(){
        heading("Entrar no PapaFila 👾");label("Login demonstrativo • não utiliza senha nem servidor.");space(12);
        EditText name=new EditText(this);name.setSingleLine(true);name.setHint("Como podemos chamar você?");name.setInputType(android.text.InputType.TYPE_CLASS_TEXT|android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS);content.addView(name);
        fullButton("Continuar",()->{String n=name.getText().toString().trim();if(n.length()<2){name.setError("Informe seu nome");return;}userName=n;prefs().edit().putString("user",n).apply();navigate("profile");});
    }
    private void showProfile(){heading("Meu perfil 👾");label("Olá, "+userName+"!");label("Sua conta é demonstrativa e fica salva neste aparelho.");space(12);
        fullButton("Ver histórico de pedidos",()->navigate("history"));
        fullButton("Acompanhar pedido atual",()->{if(currentOrder==null)Toast.makeText(this,"Nenhum pedido ativo nesta sessão",Toast.LENGTH_SHORT).show();else navigate(currentOrder.status()==Order.Status.PRONTO||currentOrder.status()==Order.Status.RETIRADO?"ready":"tracking");});
        fullButton("Sair do perfil",()->{userName="";prefs().edit().remove("user").apply();navigate("login");});
    }
    private void showHistory(){heading("Histórico de pedidos 📋");label("Pedidos salvos neste celular.");JSONArray a=history();if(a.length()==0){space(12);label("Você ainda não fez pedidos.");fullButton("Ver cardápio",()->navigate("home"));return;}
        for(int i=a.length()-1;i>=0;i--){JSONObject o=a.optJSONObject(i);if(o==null)continue;LinearLayout item=vertical();item.addView(text("Pedido "+o.optString("code"),19,true));item.addView(text(o.optString("items","Itens não registrados"),14,false));item.addView(text(money(o.optInt("total"))+" • "+o.optString("status"),15,true));item.addView(text(DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT).format(new Date(o.optLong("date"))),12,false));card(item);}
        if(currentOrder!=null)fullButton("Acompanhar último pedido",()->navigate(currentOrder.status()==Order.Status.PRONTO||currentOrder.status()==Order.Status.RETIRADO?"ready":"tracking"));
    }

}
