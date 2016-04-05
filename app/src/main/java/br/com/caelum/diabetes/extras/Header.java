package br.com.caelum.diabetes.extras;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.fragment.DashboardFragment;
import br.com.caelum.diabetes.fragment.calculadora.NovaRefeicaoFragment;
import br.com.caelum.diabetes.fragment.glicemia.NovaGlicemiaFragment;
import br.com.caelum.diabetes.fragment.perfil.ConfigurarPerfilFragment;

/**
 * Created by Fernanda Bernardo on 22/03/2016.
 */
public class Header {
    private final AppCompatActivity activity;
    private final Toolbar header;
    private Drawer drawer;

    public Header(Activity activity) {
        this.activity = (AppCompatActivity) activity;
        this.header = (Toolbar) activity.findViewById(R.id.header);
        setHeader();
    }

    private void setHeader() {
        activity.setSupportActionBar(header);
        setMenuLateral();
    }

    public void setHamburguerIcon() {
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
        header.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer();
            }
        });
    }

    public void setBackArrowIcon() {
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        header.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getSupportFragmentManager().popBackStack();
            }
        });
    }

    public void setTitulo(String titulo) {
        header.setTitle(titulo);
    }

    private void setMenuLateral() {
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("Home");
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName("Nova Refeição");
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName("Nova Glicemia");
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withName("Configurações");

        Drawer.OnDrawerItemClickListener listener = new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                switch(position) {
                    case 1:
                        transaction.replace(R.id.main_view, new DashboardFragment());
                        break;
                    case 2:
                        transaction.replace(R.id.main_view, new NovaRefeicaoFragment());
                        break;
                    case 3:
                        transaction.replace(R.id.main_view, new NovaGlicemiaFragment());
                        break;
                    case 4:
                        transaction.replace(R.id.main_view, new ConfigurarPerfilFragment());
                        break;
                }
                transaction.addToBackStack(null);
                transaction.commit();
                return false;
            }
        };

        AccountHeader perfil = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.wallpaper)
                .addProfiles(new ProfileDrawerItem()
                                .withName("Fernanda")
                                .withIcon(activity.getResources().getDrawable(R.drawable.gota))
                )
                .build();

        drawer = new DrawerBuilder().withActivity(activity)
                .withToolbar(header)
                .addDrawerItems(item1, item2, item3, item4)
                .withOnDrawerItemClickListener(listener)
                .withAccountHeader(perfil)
                .build();
    }
}
