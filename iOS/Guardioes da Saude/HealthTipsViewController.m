//
//  HealthTipsViewController.m
//  Guardioes da Saude
//
//  Created by Miqueias Lopes on 05/11/15.
//  Copyright © 2015 epitrack. All rights reserved.
//

#import "HealthTipsViewController.h"
#import "VacineViewController.h"
#import "PharmacyViewController.h"
#import "BasicCareViewController.h"
#import "EmergencyViewController.h"
#import "PreventionViewController.h"
#import "PhonesViewController.h"
#import "ZikaViewController.h"
#import "Requester.h"
#import "ViewUtil.h"
#import <Google/Analytics.h>

@interface HealthTipsViewController ()

@end

@implementation HealthTipsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    self.navigationItem.title = @"Dicas de Saúde";
    
    UIBarButtonItem *btnBack = [[UIBarButtonItem alloc]
                                initWithTitle:@""
                                style:UIBarButtonItemStylePlain
                                target:self
                                action:nil];
    
    self.navigationController.navigationBar.topItem.backBarButtonItem = btnBack;
}

-(void)viewWillAppear:(BOOL)animated{
    // GOOGLE ANALYTICS
    id<GAITracker> tracker = [[GAI sharedInstance] defaultTracker];
    [tracker set:kGAIScreenName value:@"Health Tips Screen"];
    [tracker send:[[GAIDictionaryBuilder createScreenView] build]];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (IBAction)emergency:(id)sender {
    if ([Requester isConnected]) {
        EmergencyViewController *emergencyViewController = [[EmergencyViewController alloc] init];
        [self.navigationController pushViewController:emergencyViewController animated:YES];
    }else{
        [self presentViewController:[ViewUtil showNoConnectionAlert] animated:YES completion:nil];
    }
    
}

- (IBAction)vacine:(id)sender {
    VacineViewController *vacineViewController = [[VacineViewController alloc] init];
    [self.navigationController pushViewController:vacineViewController animated:YES];
}

- (IBAction)pharmacy:(id)sender {
    if([Requester isConnected]){
        PharmacyViewController *pharmacyViewConroller = [[PharmacyViewController alloc] init];
        [self.navigationController pushViewController:pharmacyViewConroller animated:YES];
    }else{
        [self presentViewController:[ViewUtil showNoConnectionAlert] animated:YES completion:nil];
    }
}

- (IBAction)basicCare:(id)sender {
    BasicCareViewController *basicCareViewController = [[BasicCareViewController alloc] init];
    [self.navigationController pushViewController:basicCareViewController animated:YES];
}

- (IBAction)prevention:(id)sender {
    PreventionViewController *preventionViewController = [[PreventionViewController alloc] init];
    [self.navigationController pushViewController:preventionViewController animated:YES];
}

- (IBAction)phones:(id)sender {
    PhonesViewController *phonesViewController = [[PhonesViewController alloc] init];
    [self.navigationController pushViewController:phonesViewController animated:YES];
}

- (IBAction)zika:(id)sender {
    ZikaViewController *zikaViewController = [[ZikaViewController alloc] init];
    [self.navigationController pushViewController:zikaViewController animated:YES];
}
@end
