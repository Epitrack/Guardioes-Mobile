//
//  CreateAccountSocialLoginViewController.h
//  Guardioes da Saude
//
//  Created by Miqueias Lopes on 20/11/15.
//  Copyright © 2015 epitrack. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RMDateSelectionViewController.h"
#import "DateUtil.h"
@import DownPicker;

@interface CreateAccountSocialLoginViewController : UIViewController<UITextFieldDelegate, UITextViewDelegate>
@property (weak, nonatomic) IBOutlet UITextField *txtNick;
@property (weak, nonatomic) IBOutlet UITextField *txtEmail;
- (IBAction)btnCadastrar:(id)sender;
- (IBAction)backButtonAction:(id)sender;
@property (weak, nonatomic) IBOutlet UIDownPicker *downGenre;
@property (weak, nonatomic) IBOutlet UIDownPicker *downRace;
@property (weak, nonatomic) IBOutlet UIButton *btnDate;
- (IBAction)touchedButton:(id)sender;
- (IBAction)action:(id)sender;



@end
