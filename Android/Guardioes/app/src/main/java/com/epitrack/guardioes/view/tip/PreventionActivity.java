package com.epitrack.guardioes.view.tip;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;

import butterknife.Bind;

/**
 * @author Igor Morais
 */
public class PreventionActivity extends BaseAppCompatActivity {

    @Bind(R.id.prevention_content_01)
    TextView preventContent;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.prevention);

        preventContent.setText(Html.fromHtml("<p><b>Malária</b></p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;No Brasil, a transmissão concentra-se na Região Amazônica, composta pelos estados do Acre, Amapá, Amazonas, Maranhão, Mato Grosso, Pará, Rondônia, Roraima e Tocantins.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Em áreas de transmissão de malária, é fundamental que o viajante tenha conhecimento sobre o horário de maior atividade de mosquitos vetores de malária, do pôr-do-sol ao amanhecer.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Use roupas claras e com manga longa, durante atividades de exposição elevada.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Aplique repelente nas áreas expostas da pele, seguindo a orientação do fabricante. Em crianças com idade inferior a dois anos, não é recomendado o uso de repelente sem orientação médica.</p>\n" +
                "<p>&#8226;&nbsp;Em áreas de transmissão de malária é imprescindível que o viajante fique atento ao surgimento de sintomas da doença, como febre, dor no corpo e dor de cabeça.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Em caso de manifestação de algum sintoma da doença, procure uma unidade de saúde especializada mais próxima, o ideal é que este atendimento seja feito o quanto antes, em até 48 horas após os primeiros sintomas.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;O Brasil conta com uma rede pública de saúde estruturada para diagnosticar e tratar os pacientes, de forma oportuna e adequada.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Caso você queira adquirir mais informações acerca da malária antes de realizar a sua viagem, busque orientações em um dos Centros de Referência.</p>\n" +
                "\n" +
                "\n" +
                "<p><b>Previna-se contra doenças de transmissão respiratória</b></p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Lavar as mãos com água e sabonete antes das refeições, de tocar os olhos, a boca e o nariz e após tossir, espirrar ou usar o banheiro.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Indivíduos doentes devem ficar em repouso, consumir alimentação balanceada, aumentar a ingestão de líquidos e evitar aglomerações e ambientes fechados.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Mantenha os ambientes ventilados.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Esteja sempre atento ao apresentar sintomas respiratórios de maior gravidade e procure imediatamente assistência médica.</p>\n" +
                "\n" +
                "\n" +
                "<p><b>Consuma alimentos e bebidas de forma saudável</b></p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Evite consumir alimentos cujas condições higiênicas, de preparo e acondicionamento, são precárias.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Prefira alimentos que contenham baixo teor de açúcares, gorduras e sal.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Beba bastante líquido, preferencialmente água ou suco. Evite alimentos crus ou mal cozidos, principalmente os frutos do mar.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Alimentos embalados devem conter no rótulo a identificação do produtor, data de validade e a embalagem deve estar íntegra;</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Durante o turismo rural, dê preferência aos alimentos que podem ficar sem refrigeração e não estraguem com o calor;</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Caso tenha diarreia e vômitos por conta da ingestão de alimentos e bebidas, é preciso cuidado redobrado com a desidratação. Recomenda-se a ingestão de sal de reidratação oral, disponibilizado gratuitamente pelo Sistema Único de Saúde, ou outras soluções do tipo. As bebidas esportivas não compensam corretamente as perdas e não devem ser utilizadas para tratamento de doença diarreica.</p>\n" +
                "\n" +
                "<p><b>Pratique sexo de forma segura</b></p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;No Brasil, a camisinha é distribuída gratuitamente pelo governo e está disponível em postos de saúde, unidades de pronto atendimento, albergues, Centros de Atendimento ao Turista e em outros pontos de distribuição espalhados pelas cidades. Leve sempre consigo os preservativos para facilitar a sua utilização e a prevenção de doenças sexualmente transmissíveis.</p>\n"));

    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Prevention Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }
}
