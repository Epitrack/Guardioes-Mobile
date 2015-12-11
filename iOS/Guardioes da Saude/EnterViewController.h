//
//  EnterViewController.h
//  Guardioes da Saude
//
//  Created by Miqueias Lopes on 06/11/15.
//  Copyright © 2015 epitrack. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface EnterViewController : UIViewController<UITextViewDelegate, UITextFieldDelegate>
@property (weak, nonatomic) IBOutlet UITextField *txtEmail;
@property (weak, nonatomic) IBOutlet UITextField *txtPassword;
- (IBAction)btnEnter:(id)sender;
- (IBAction)iconBackAction:(id)sender;
- (IBAction)forgotPasswordAction:(id)sender;

@end
