/*
 * Copyright 2015 Hongxin Telecommunication Technologies Co, Ltd.,
 * Wuhan, Hubei, China. All rights reserved.
 */

/*
 * AcDetailDialog.java
 *
 * Created on 2017-8-7, 16:46:38
 */

package com.hxct.smartnotify;

import com.hxct.entity.AcEntity;
import javax.swing.JOptionPane;

/**
 *
 * @author chenwei
 */
public class AcDetailDialog extends javax.swing.JDialog
{
	private MainFrame mf;
	private AcEntity acEntity;

    /** Creates new form AcDetailDialog */
    public AcDetailDialog(MainFrame parent, boolean modal, AcEntity acEntity)
	{
        super(parent, modal);
        initComponents();
		this.mf = parent;
		this.acEntity = acEntity;
		acName.setText(acEntity.getName());
		acIpAddr.setText(acEntity.getIpaddr());
		acModel.setText(acEntity.getModel());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLabel1 = new javax.swing.JLabel();
        acName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        acIpAddr = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        acModel = new javax.swing.JTextField();
        modifyAcBtn = new javax.swing.JButton();
        close = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("修改AC");

        jLabel1.setText("AC名称");

        acName.setMinimumSize(new java.awt.Dimension(100, 21));
        acName.setPreferredSize(new java.awt.Dimension(100, 21));

        jLabel2.setText("AC IP地址");

        acIpAddr.setPreferredSize(new java.awt.Dimension(80, 21));

        jLabel5.setText("设备型号");

        modifyAcBtn.setText("修改");
        modifyAcBtn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                modifyAcBtnActionPerformed(evt);
            }
        });

        close.setText("关闭");
        close.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                closeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(acName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addGap(22, 22, 22)
                        .addComponent(acIpAddr, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE))
                    .addComponent(acModel, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE))
                .addGap(20, 20, 20))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(modifyAcBtn)
                .addGap(18, 18, 18)
                .addComponent(close)
                .addGap(22, 22, 22))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(acName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(acIpAddr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(acModel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(modifyAcBtn)
                    .addComponent(close))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeActionPerformed
    {//GEN-HEADEREND:event_closeActionPerformed
       dispose();
    }//GEN-LAST:event_closeActionPerformed

    private void modifyAcBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_modifyAcBtnActionPerformed
    {//GEN-HEADEREND:event_modifyAcBtnActionPerformed
        AcEntity ace = new AcEntity();
		ace.setIpaddr(acIpAddr.getText());
		ace.setModel(acModel.getText());
		ace.setName(acName.getText());
		ace.setId(acEntity.getId());
		ace.copyFrom(acEntity);
		if(mf.modifyAcInfo(ace)){
			JOptionPane.showMessageDialog(this, "修改成功!");
			dispose();
		}
    }//GEN-LAST:event_modifyAcBtnActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[])
	{
        java.awt.EventQueue.invokeLater(new Runnable()
		{
            public void run()
			{
                AcDetailDialog dialog = new AcDetailDialog(null, true,null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter()
				{
                    public void windowClosing(java.awt.event.WindowEvent e)
					{
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField acIpAddr;
    private javax.swing.JTextField acModel;
    private javax.swing.JTextField acName;
    private javax.swing.JButton close;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JButton modifyAcBtn;
    // End of variables declaration//GEN-END:variables

}
